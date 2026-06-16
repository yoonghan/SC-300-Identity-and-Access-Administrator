# Role based access

## Azure RBAC
1. Roles for Azure Resources.
2. Example: VM Contributor role.

## Microsoft Entra RBAC
1. Only for Users, Group, Domains and Applications.
2. This is custom roles for "App Registration" and "Enterprise Application".

## User assigned roles
1. Create user assigned role
2. Assign user assigned role
3. User can assign a role type:
    - Custom Roles
    - Defined roles for apps
        - Application developer
        - Application administrator
4. Scope Type:
    - Directory = Top level of tenant. Meaning user have all the access to all application, resources with the selected role type.
    - Application = Targeted boundry, meaning user can only assign role type to application that is assign to him. This can be configured when the role is created.
5. Important note, user assigned roles can only assign **other users** to roles, not 


## Flow in Azure
### App Registration
1. Every app created must "register" in Microsoft Entra 
2. Here the option are of "who can use this app":
    - Single tenant: Only for your organization.
    - Multitenant: For your organization and other organizations.
    - Multitenant and Microsoft accounts: For your organization and Microsoft accounts.
3. (Optional) There are options to re-direct the uri
4. After Registration, you will get an "Application (client) ID", you must save it, as it is used to identify the app in the tenant.
5. Can define scopes, API permissions and assign roles here.
6. Additionally it creates a (tenant) **service principal** in the tenant.
7. In a multi tenant env, if an app is used by another org, they will have to "accept" the app, and it will create another "service principal" in their tenant referencing to this application id.

### Enterprise Application
1. Enterprise application is the instance of an app in a tenant, or it's a list of "service principal".
2. It is used to manage the app in a tenant.
3. Please take note in multi-tenant, it can only view/manage **it's own tenant's service principal**. E.g. if tenant A registers to this app, then only tenant A can view/manage it's service principal and not viewable or controllable by this tenant's admin. This is to protect the privacy of the other tenant.
4. **Note**: Enterprise application has a toggle "Assignment required", to control who can access this app. If it is ON, then need to assign user/group to access this app. If it is OFF, then anyone in your tenant can access this app.

### How both App registration and enterprise application work together
1. App registration first register and configure "API Permissions", e.g. get Group.Read.All from Microsoft Graph API.
2. Enterprise Application has to review and "Grant Admin Consent" to allow this API permission to be used by the service principal.
3. Once the Admin Consent is granted, the service principal can use the API permission to access the Microsoft Graph API.
4. Finally, assign the role to the user/group in the Enterprise Application.

