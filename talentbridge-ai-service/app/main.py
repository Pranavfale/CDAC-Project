"""Receive offer facts from Spring Boot, generate content with Groq, and save a PDF."""

from datetime import datetime
from pathlib import Path
import re
from xml.sax.saxutils import escape

from fastapi import FastAPI, HTTPException
import httpx
from pydantic import BaseModel, Field
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.lib.units import mm
from reportlab.platypus import Paragraph, SimpleDocTemplate, Spacer

from app.config import GROQ_API_KEY, GROQ_MODEL, PDF_OUTPUT_DIRECTORY


app = FastAPI(title="TalentBridge AI PDF Service", version="1.0.0")


class OfferRequest(BaseModel):
    """Verified offer details sent by Spring Boot."""

    offerId: str = Field(min_length=1, max_length=100)
    candidateName: str = Field(min_length=1, max_length=200)
    jobTitle: str = Field(min_length=1, max_length=200)
    department: str = Field(min_length=1, max_length=200)
    salary: str = Field(min_length=1, max_length=200)
    joiningDate: str = Field(min_length=1, max_length=100)
    workLocation: str = Field(min_length=1, max_length=300)
    companyName: str = Field(min_length=1, max_length=300)
    companyAddress: str = Field(min_length=1, max_length=1000)
    hrName: str = Field(min_length=1, max_length=200)
    benefits: list[str] = Field(default_factory=list)
    additionalTerms: list[str] = Field(default_factory=list)


def safe_file_part(value: str) -> str:
    """Convert input text into a safe part of a file name."""

    cleaned = re.sub(r"[^A-Za-z0-9_-]+", "_", value.strip())
    return cleaned.strip("_") or "offer"


def generate_offer_content(data: OfferRequest) -> str:
    """Ask Groq to write the offer letter using only Spring Boot data."""

    if not GROQ_API_KEY:
        raise RuntimeError("GROQ_API_KEY is not configured.")

    prompt = f"""
Write a professional and concise employment offer letter.
Use only the facts supplied below. Do not invent salary, dates, benefits, terms,
job details, or company details. Return plain text only, without markdown.
Do not add the HR signature because it will be added to the PDF separately.

Candidate: {data.candidateName}
Job title: {data.jobTitle}
Department: {data.department}
Salary / CTC: {data.salary}
Joining date: {data.joiningDate}
Work location: {data.workLocation}
Company: {data.companyName}
Company address: {data.companyAddress}
Benefits: {', '.join(data.benefits) if data.benefits else 'None provided'}
Additional terms: {', '.join(data.additionalTerms) if data.additionalTerms else 'None provided'}
""".strip()

    response = httpx.post(
        "https://api.groq.com/openai/v1/chat/completions",
        headers={
            "Authorization": f"Bearer {GROQ_API_KEY}",
            "Content-Type": "application/json",
        },
        json={
            "model": GROQ_MODEL,
            "messages": [
                {
                    "role": "system",
                    "content": "You write factual employment offer letters from supplied data.",
                },
                {"role": "user", "content": prompt},
            ],
            "temperature": 0.2,
        },
        timeout=30.0,
    )

    if response.status_code != 200:
        raise RuntimeError(f"Groq request failed: {response.text}")

    content = response.json()["choices"][0]["message"]["content"]
    if not content or not content.strip():
        raise RuntimeError("Groq returned empty offer content.")

    return content.strip()


def create_pdf(data: OfferRequest, ai_content: str) -> Path:
    """Create the PDF in the configured local directory."""

    output_directory = PDF_OUTPUT_DIRECTORY.expanduser().resolve()
    output_directory.mkdir(parents=True, exist_ok=True)

    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    file_name = (
        f"{safe_file_part(data.candidateName)}_offer_"
        f"{safe_file_part(data.offerId)}_{timestamp}.pdf"
    )
    pdf_path = output_directory / file_name

    document = SimpleDocTemplate(
        str(pdf_path),
        pagesize=A4,
        leftMargin=25 * mm,
        rightMargin=25 * mm,
        topMargin=22 * mm,
        bottomMargin=22 * mm,
        title="Offer Letter",
        author=data.companyName,
    )

    styles = getSampleStyleSheet()
    story = [
        Paragraph(escape(data.companyName), styles["Title"]),
        Paragraph(escape(data.companyAddress), styles["BodyText"]),
        Spacer(1, 18),
        Paragraph("OFFER LETTER", styles["Heading1"]),
        Spacer(1, 8),
    ]

    # Preserve Groq paragraph breaks while escaping unsafe characters.
    for paragraph in re.split(r"\n\s*\n", ai_content):
        clean_paragraph = escape(paragraph.strip()).replace("\n", "<br/>")
        if clean_paragraph:
            story.extend([Paragraph(clean_paragraph, styles["BodyText"]), Spacer(1, 10)])

    story.extend(
        [
            Spacer(1, 12),
            Paragraph("Sincerely,", styles["BodyText"]),
            Paragraph(escape(data.hrName), styles["BodyText"]),
            Paragraph("Human Resources", styles["BodyText"]),
            Paragraph(escape(data.companyName), styles["BodyText"]),
        ]
    )

    document.build(story)
    return pdf_path


@app.post("/api/offers/generate-ai-pdf")
def generate_ai_pdf(data: OfferRequest) -> dict[str, str]:
    """Generate AI content, save its PDF locally, and return the file path."""

    try:
        ai_content = generate_offer_content(data)
        pdf_path = create_pdf(data, ai_content)
        return {
            "status": "SUCCESS",
            "message": "AI offer PDF generated successfully.",
            "aiContent": ai_content,
            "fileName": pdf_path.name,
            "filePath": str(pdf_path),
        }
    except Exception as error:
        raise HTTPException(status_code=500, detail=str(error)) from error
