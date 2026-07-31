# TalentBridge AI Service

Internal Python microservice for controlled AI-assisted offer-letter drafting.

## Port

`8000`

## Request source

This service accepts internal requests only from the TalentBridge Spring Boot backend.

## Responsibilities

- Validate internal service requests
- Build controlled offer-letter prompts
- Call the configured AI provider
- Return structured JSON offer content
- Support generation, rewriting, and regeneration
- Validate provider responses
- Return controlled errors
- Provide an internal health endpoint

## Restrictions

This service must not:

- Access the TalentBridge MySQL database
- Receive requests directly from React
- Receive requests through the Node.js API Gateway
- Authenticate public users
- Generate JWTs
- Rank or score candidates
- Select or reject candidates
- Decide compensation
- Decide joining or expiry dates
- Approve AI content
- Generate PDFs
- Send emails
- Change recruitment statuses

## Local start command

```powershell
python -m uvicorn app.main:app --host 127.0.0.1 --port 8000 --reload