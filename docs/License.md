# License
A good [read up](https://learn.microsoft.com/en-us/training/modules/create-configure-manage-identities/9-exercise-change-group-license-assignments)

## License Assignment
1. Can be managed through:
    - Microsoft 365 admin center
    - PowerShell
    - Microsoft Graph API
2. Some Microsoft services aren't available in all locations. The administrator, before assigning a license to a user, should specify usage location in the User Profile.

## Assigning license
![License assignment to Users](img/license-assignment-to-users.png)
    
2. Select Billing from the menu on the left.
3. Select Licenses.
4. From the list of licenses you have available, select one.
5. Select Groups from the list near the top of the screen.
6. On the Groups page, select + Assign license.

## Group
1. You must have one of the following licenses to use group-based licensing:
    - Paid or trial subscription for Microsoft Entra ID Premium P1 and greater
    - Paid or trial edition Office 365 Enterprise E3 or greater
2. For group license assignment, any users without a usage location specified inherit the **location of the directory**. If you have users in multiple locations, we recommend that you always set usage location as part of your user creation.

## Feature
1. Licenses can be assigned to any security group in Microsoft Entra ID. Security groups can be synced from on-premises by using:
    - Microsoft Entra Cloud Sync (recommended)
    - Microsoft Entra Connect Sync.
2. Group-based licensing is currently available ONLY through the Microsoft 365 admin center!
3. User can be a member of multiple groups with license policies specified. A user can also have some licenses that were directly assigned, outside of any groups. 