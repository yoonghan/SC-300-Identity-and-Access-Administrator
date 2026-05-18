# License
A good [read up](https://learn.microsoft.com/en-us/training/modules/create-configure-manage-identities/9-exercise-change-group-license-assignments)

## License Assignment
1. Can be managed through:
    - Microsoft 365 admin center
    - PowerShell
    - Microsoft Graph API
2. Some Microsoft services aren't available in all locations. The administrator, before assigning a license to a user, should specify usage location in the User Profile.
3. License are technically assigned to users. But you can use Security Group (recommended) which will auto assign to user even for new users. License assignment can also be done for dynamic group.
4. License are also location specific. User must specify usage location in User Profile, else it will use the tenant's default location.
5. Microsoft Entra Identity Protection and Privileged Identity Management (PIM) can be used for license assignment to groups. But both are included ONLY in P2 license.

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
4. Only with license P1/P2 can have:
    - Self-service password reset with writeback.

## Unorganized note
Licensing is notoriously one of the most frustrating parts of the Microsoft ecosystem, but for the SC-300 exam, you must know exactly which feature requires which license.To clear up the confusion for your developer brain, let's use a simple architectural analogy: Entra ID P1 and P2 are the "features/engines." Microsoft 365 E3 and E5 are the "cardboard boxes" that bundle those engines together with other software (like Word, Excel, Exchange, and Windows).Guideline 1: The "Box vs. Engine" MappingInstead of trying to memorize everything, memorize the core combinations. Microsoft 365 (M365) suites always include specific Entra ID tiers:Microsoft 365 E3 includes Entra ID P1. (It does not include P2).  Microsoft 365 E5 includes Entra ID P2.  Note on Office 365: If a company uses Office 365 E3, it actually only includes Entra ID Free. They have to buy Entra ID P1 as a standalone add-on. But since your question is specifically about Microsoft 365 E3, you automatically have Entra ID P1.  Guideline 2: Distinguishing P1 vs. P2 (The "Logic" Rule)As an architect, you can distinguish P1 and P2 features instantly by looking at whether the security action is Static or Dynamic/Automated.Entra ID P1 = Static Policies & Manual RulesP1 is your baseline enterprise security. It gives you all the core "building blocks" you need to secure a standard company, but you have to write the rules manually.  Conditional Access (Static): "If user is in the Finance Group AND on an unmanaged laptop, require MFA."Dynamic Groups: Auto-assign users based on static attributes (department -eq "Sales").SSPR (Hybrid Writeback): This answers your exact question! Self-Service Password Reset with on-premises writeback is a P1 feature. Therefore, a user with a Microsoft 365 E3 license can absolutely use it.  Entra ID P2 = Dynamic Risk, Machine Learning, and GovernanceP2 takes everything in P1 and inserts automated logic, telemetry, and strict lifecycle governance on top of it.  Conditional Access (Risk-Based): P1 checks where you are; P2 checks how weird your login behavior is. It uses ML to detect "Impossible Travel" (e.g., signing in from Singapore, then 10 minutes later from Hong Kong) or if your password was leaked on the dark web.  Privileged Identity Management (PIM): This is the biggest P2 exam topic. No one has standing admin rights. Admins must "request" to activate their Global Admin role for 2 hours, requiring approval.Access Reviews: Automated quarterly audits that ask managers, "Does John still need access to this sensitive database container?"Quick Cheat Sheet for the SC-300 ExamIf you see these keywords on the exam, match them to the license immediately:Feature / KeywordMinimum Required LicenseIncluded in M365 E3?Included in M365 E5?Conditional Access (Standard)P1YesYesSSPR with On-Prem WritebackP1YesYesDynamic GroupsP1YesYesApplication ProxyP1YesYesRisk-Based Conditional AccessP2NoYesPrivileged Identity Management (PIM)P2NoYesAccess Reviews / GovernanceP2NoYesPractical Summary for Your ScenarioIf a client tells you: "We have Microsoft 365 E3, and we want to deploy Self-Service Password Reset that syncs back to our local Active Directory," your architect answer is: "Yes, we can design and build that immediately because M365 E3 includes Entra ID P1, which fully supports password writeback."  But if they say: "We have Microsoft 365 E3, and we want our developers to use PIM to get temporary access to production," your answer is: "We need to buy standalone Entra ID P2 add-on licenses for those developers, or step them up to M365 E5."Does framing it as P1 = Static/Manual and P2 = Dynamic/Risk/Governance help you categorize these features while you're away?