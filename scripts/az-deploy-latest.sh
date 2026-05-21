#!/usr/bin/env bash

# Force Azure Container App to pull the latest image by creating a new revision with a unique suffix.
# This preserves all existing configurations, environment variables, secrets, and Service Connector links (like App Insights).

echo "Deploying the latest image to Azure Container App..."

az containerapp update \
  --name sc-300-quiz-gen \
  --resource-group walcron-rg \
  --image ghcr.io/yoonghan/sc-300-identity-and-access-administrator:latest \
  --revision-suffix "rev$(date +%s)"

echo "Latest image deployed and new revision created successfully!"
