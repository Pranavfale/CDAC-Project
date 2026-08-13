import os
from pathlib import Path

GROQ_API_KEY = os.getenv("GROQ_API_KEY", "")

GROQ_MODEL = os.getenv("GROQ_MODEL", "llama-3.3-70b-versatile")

PDF_OUTPUT_DIRECTORY = Path(
    os.getenv("PDF_OUTPUT_DIRECTORY", "offer-pdfs")
)