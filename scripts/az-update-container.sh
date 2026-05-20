# Currently using latest, so just bumping it is enough.
# TODO going forward, will use github pipeline.

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
  echo "Usage: $0 <username> <password> <gemini_api_key>"
  exit 1
fi

GEMINI_API_KEY=$3

# Ensure we're in the right directory to find the containerapp.yaml
SCRIPT_DIR="$(dirname "$0")"

TEMP_YAML=$(mktemp)
sed -e "s/YOUR_GEMINI_API_KEY/$GEMINI_API_KEY/g" \
    -e "s/YOUR_REGISTRY_USERNAME/$1/g" \
    -e "s/YOUR_REGISTRY_PASSWORD/$2/g" \
    "$SCRIPT_DIR/containerapp.yaml" > $TEMP_YAML

az containerapp update \
  --name sc-300-quiz-gen \
  --resource-group walcron-rg \
  --yaml $TEMP_YAML

rm $TEMP_YAML

echo "Linking Application Insights to Container App..."
az containerapp connection create app-insights \
  --resource-group walcron-rg \
  --name sc-300-quiz-gen \
  --target-resource-group walcron-rg \
  --app-insights walcron-application-insight \
  --client-type java \
  --container sc-300-quiz-gen