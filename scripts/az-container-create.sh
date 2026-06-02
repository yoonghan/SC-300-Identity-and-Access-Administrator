# az-container-create.sh yoonghan 

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
  echo "Usage: $0 <username> <password> <gemini_api_key>"
  exit 1
fi

GEMINI_API_KEY=$3
# 1. Create Log Analytics Workspace explicitly to link to Application Insights
echo "Creating Log Analytics Workspace..."
az monitor log-analytics workspace create \
  --resource-group walcron-rg \
  --workspace-name walcron-log-workspace \
  --location southeastasia

LOG_WORKSPACE_ID=$(az monitor log-analytics workspace show --resource-group walcron-rg --workspace-name walcron-log-workspace --query id -o tsv)
LOG_WORKSPACE_CLIENT_ID=$(az monitor log-analytics workspace show --resource-group walcron-rg --workspace-name walcron-log-workspace --query customerId -o tsv)
LOG_WORKSPACE_SECRET=$(az monitor log-analytics workspace get-shared-keys --resource-group walcron-rg --workspace-name walcron-log-workspace --query primarySharedKey -o tsv)

# 2. Create the Container App Environment (The 'Sandbox')
echo "Creating Container App Environment..."
az containerapp env create \
  --name walcron-env \
  --resource-group walcron-rg \
  --location southeastasia \
  --logs-workspace-id $LOG_WORKSPACE_CLIENT_ID \
  --logs-workspace-key $LOG_WORKSPACE_SECRET

# 3. Create Application Insights linked to the Log Analytics Workspace
echo "Creating Application Insights component..."
az monitor app-insights component create \
  --app walcron-application-insight \
  --location southeastasia \
  --kind web \
  --resource-group walcron-rg \
  --workspace $LOG_WORKSPACE_ID

# 4. Deploy the App with Scale-to-Zero using YAML configuration
echo "Deploying Container App..."

TEMP_YAML=$(mktemp)
sed -e "s/YOUR_GEMINI_API_KEY/$GEMINI_API_KEY/g" \
    -e "s/YOUR_REGISTRY_USERNAME/$1/g" \
    -e "s/YOUR_REGISTRY_PASSWORD/$2/g" \
    containerapp.yaml > $TEMP_YAML

az containerapp create \
  --name sc-300-quiz-gen \
  --resource-group walcron-rg \
  --environment walcron-env \
  --yaml $TEMP_YAML

rm $TEMP_YAML

# 5. Create Application connection
echo "Linking Application Insights to Container App..."
az containerapp connection create app-insights \
  --resource-group walcron-rg \
  --name sc-300-quiz-gen \
  --target-resource-group walcron-rg \
  --app-insights walcron-application-insight \
  --client-type java \
  --container sc-300-quiz-gen