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

## Entra vs AD
1. Microsoft Entra ID is primarily an identity solution, and it’s designed for internet-based applications by using HTTP (port 80) and HTTPS (port 443) communications.
2. Microsoft Entra ID is a multi-tenant directory service.
3. Microsoft Entra users and groups are created in a flat structure, and there are no OUs or GPOs.
4. You can't query Microsoft Entra ID by using LDAP; instead, Microsoft Entra ID uses the REST API over HTTP and HTTPS.
5. Microsoft Entra ID doesn't use Kerberos authentication; instead, it uses HTTP and HTTPS protocols such as SAML, WS-Federation, and OpenID Connect for authentication, and uses OAuth for authorization.
6. Microsoft Entra ID includes federation services, and many third-party services such as Facebook are federated with and trust Microsoft Entra ID.

## Administrative Units
Administrative units (AUs) are containers that you can use to delegate administrative control over specific sets of users and groups. For example, you can create an AU for a specific department or location, and then assign an administrator to manage the users and groups in that AU.

### Restrictive Administrative Units
Only users and groups that are members of the AU can be managed by the administrator. Only **Global Administrator** can add members to the AU and **User Administrator** can manage members of the AU.

## Users
1. Cloud Identities - Users that are only in Entra ID.
2. Directory Synced Identities - Users that are synced from on-premises AD to Entra ID. **Microsoft Entra Cloud Sync** is the recommended synchronization tool for most organizations—it uses a lightweight cloud-managed agent and supports multiple disconnected forests. **Microsoft Entra Connect Sync** remains available for complex scenarios such as device synchronization or groups with more than 50,000 members. Their source is **Windows Server AD**.
3. Hybrid Identities - Users that are synced from on-premises AD to Entra ID and have a cloud-based identity.
4. Guest Identities - Users that are not members of the organization but have access to resources in the organization.  

### Delete Users
1. Soft Delete - Users are soft deleted and can be restored within 30 days.
2. Hard Delete - Users are hard deleted and cannot be restored.
3. Permission required to restore or delete users is **User Administrator** or **Global Administrator** or **Partner Tier 1 Support** or **Partner Tier 2 Support**.

## Groups
2. Microsoft 365 Groups - Groups that are used to collaborate with other users. They have a shared mailbox, calendar, and other resources. This option also lets you give people outside of your organization access to the group. This option is available to users and admins.
3. Security Groups - Groups that are used to manage access to resources. Members of a security group can include users, devices, and service principals. This option requires a Microsoft Entra administrator.
4. Dynamic Security Groups - Groups that are dynamically populated based on rules:
    - **Dynamic User** rules can be created for users, such as department, job title, or location.
    - **Dynamic Device** rules can be created for devices, require Microsoft Entra ID P1 or P2.
    - **Conditional Access Policy** - control access to resources based on conditions.

## Microsoft Entra joined devices
1. Any organization can deploy Microsoft Entra joined devices no matter the size or industry. 
2. Controlled with Mobile Device Management.
3. Can join as hybrid with on-prem AD.
4. No longer supports device writeback. Use Cloud Kerberos Trust which allows Microsoft Entra joined and hybrid joined devices to authenticate to on-premises resources without requiring device objects to be written back to on-premises Active Directory.


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
