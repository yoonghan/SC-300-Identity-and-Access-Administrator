# Azure Quiz Generator
This is a tool to generate quiz questions for SC-300 exam.

## Usage
1. Set your API key in the environment variable `GEMINI_API_KEY`.
2. Run the application.

## Local docker
1. Run 
```
docker build -t azure-quizer .

docker run -d -p 8080:8080 \
  -e GEMINI_API_KEY=your-api-key \
  --name my-azure-quizer azure-quizer
```