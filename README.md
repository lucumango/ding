# Ding: Serverless Email Tracker

A serverless application built with Java, AWS Lambda, API Gateway, and DynamoDB to track email opens using a 1x1 invisible pixel.

## Architecture

- **AWS Lambda (Java 21):** Handles the tracking logic (saves to DB and returns the transparent pixel).
- **Amazon API Gateway:** Exposes the HTTP endpoint for the email clients to fetch the image.
- **Amazon DynamoDB:** NoSQL database to store the tracking logs.

## Setup

1. Configure your AWS credentials.
2. Build the project using Maven: `mvn clean package`
3. Deploy using AWS SAM or Serverless Framework.

