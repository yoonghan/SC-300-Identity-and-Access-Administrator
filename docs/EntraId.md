# Entra ID
Also know as Active Directory Domain Service(AD DS) previously. It deals only with authentication and identity. This is covered in [SC-300](https://learn.microsoft.com/en-us/credentials/certifications/identity-and-access-administrator/?practice-assessment-type=certification).

## Capabilities
- Configuring access to applications
- Configuring single sign-on (SSO) to cloud-based SaaS applications
- Managing users and groups
- Provisioning users
- Enabling federation between organizations
- Providing an identity management solution
- Identifying irregular sign-in activity
- Configuring multi-factor authentication
- Extending existing on-premises Active Directory implementations to Microsoft Entra ID
- Configuring Application Proxy for cloud and local applications
- Configuring Conditional Access for users and devices

## Supplementary Notes
1. Microsoft 365 comes with Entra ID.
2. Entra ID is a cloud-based identity and access management service.
3. Entra ID have tiers
    - Free
    - Microsoft Entra ID P1
    - Microsoft Entra ID P2
    - Microsoft Entra ID Governance
4. Unlike AD it doesn't store Computer name but device class.
5. All Tenant have a onmicrosoft.com domain name. It is the default domain name. You can add custom domain name.
6. AD is NOT DEAD, you can use it on computers; i.e even installing into VM (just not on drive C:).


```mermaid
flowchart TD
    Tenant --> Subscription --> "Administrative Units(Optional)" --> "Group (Optional)" --> "User / Device"
```

## Administrative Units
Administrative units (AUs) are containers that you can use to delegate administrative control over specific sets of users and groups. For example, you can create an AU for a specific department or location, and then assign an administrator to manage the users and groups in that AU.

### Restrictive Administrative Units
Only users and groups that are members of the AU can be managed by the administrator. Only **Global Administrator** can add members to the AU and **User Administrator** can manage members of the AU.

## License assignment to groups
1. License assignment to groups is managed through (underneath is still Entra ID):
    - Microsoft 365 admin center.
    - PowerShell.
    - Graph API.
2. Requirements
    - Paid or trial subscription for Microsoft Entra ID Premium P1 and greater
    - Paid or trial edition Office 365 Enterprise E3 or greater
3. License are assigned to groups for P1 license. For P2 license, it is assigned to users.
4. License are also location specific. User must specify usage location in User Profile, else it will use the tenant's default location.
5. Microsoft Entra Identity Protection and Privileged Identity Management (PIM) can be used for license assignment to groups. But both are included ONLY in P2 license.

### Microsoft Entra ID Protection
- What it does: Uses machine learning to detect suspicious activities (e.g., impossible travel, leaked credentials) to prevent compromised account access.
- Capabilities: Generates risky user reports and allows for automated remediation via risk-based Conditional Access policies.

### Microsoft Entra Privilege Identity Management (PIM)
- What it does: Mitigates risk by enforcing "least privilege" access, allowing administrators to activate permissions only when needed, rather than having standing admin access.
- Capabilities: Time-bound activation, approval workflows for activation, and auditing of privileged roles.

## Custom Security Attributes
1. Custom security attributes are key-value pairs that you can use to store additional information about users and groups.
2. They are defined by an administrator and can be used to filter and group users and groups.
3. They are also used to control access to resources based on conditions.
4. Custom security attributes are not supported for guest users.

## System for Cross-Domain Identity Management (SCIM)
Main goal is to automate the provisioning and deprovisioning of users and groups between systems.
1. SCIM is an open standard for automating the exchange of user identity information between systems.
2. It is used to provision and deprovision users and groups between systems.
3. It is also used to manage user attributes and group memberships.

### New feature - Automatic provisioning
Microsoft Entra ID supports **API-driven inbound provisioning**, which reached general availability in March 2024. Instead of requiring the source system to push data via SCIM, any automation tool, or script can retrieve workforce data from any system of record and send it to the Microsoft Entra provisioning API. 
