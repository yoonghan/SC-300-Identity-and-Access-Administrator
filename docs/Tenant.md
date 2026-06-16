# Tenant

## Tenant Type
There are basically only 2 tenant types. 
  - Microsoft Entra ID
  - Azure AD B2C (this is the old External ID, deprecated but in exam)
  - Microsoft Entra External ID (this is the new AD B2C)

| Architectural Property | Standard Entra ID Tenant (Workforce) | Microsoft Entra External ID Tenant |
| --- | --- | --- |
| **Primary User Type** | Employees, Contractors, Internal Staff | Consumers, Retail Customers, B2B Partners |
| **Identity Providers** | Organization Credentials, Federated Corporate ID | Social Logins (Google, Facebook, Apple), Local Email/Pass |
| **Device Trust** | Intune MDM, Compliant Devices, Hybrid Join | None. You cannot force a consumer's personal Mac or Android to be compliant. |
| **Pricing Model** | Per-User seat license (Free, P1, P2) | MAU (Monthly Active Users) — Free for the first 50,000 active users per month. |

### For Entra External/AD

When a public customer signs up via your HTMX frontend using their personal email (e.g., customer@gmail.com), Entra ID creates that object right inside the consumer directory database.

Because that tenant belongs to your customers, they are created with a **UserType** of **Member**.

They are not "guests" visiting an enterprise; they are the primary citizens that the external tenant was built to store.

The Member Privilege: Because your consumer users are technically Members inside that specific External ID tenant directory, they can read standard directory properties (like looking up their own object ID or profile details via the Microsoft Graph API) which a Guest account would be programmatically blocked from doing without explicit configuration.

The Authentication Suffix: Even though their UserType is Member, their User Principal Name (UPN) will be uniquely formatted by the directory engine to handle routing (e.g., customer_gmail.com#EXT#@yourtenant.onmicrosoft.com).


[ Standard Workforce Tenant ] ──► Created Employee ──► Type: Member
                             ──► Invited Partner  ──► Type: Guest

[ Entra External ID Tenant ]  ──► Retail Customer  ──► Type: Member

## Roles
- Global Administrator - have all the permissions including managing other administrator roles. Can also invite external user.
- See here (right menu is the roles) - https://learn.microsoft.com/en-us/entra/identity/role-based-access-control/permissions-reference#global-administrator
- Custom roles is only for those have licensed P1/P2.
- Custom roles can only clone from other custom role, not build-in roles.

### Custom Roles for External ID
1. This is powerful as it can split to:
    - Directory Admin Layer
    - Application Layer
2. Application layer is powerful as you can split user to tiers. E.g. paid user get some extra privilege on your app. You can define custom roles (App Roles) right inside your application registration (e.g., Quiz.PremiumSubscriber or Quiz.ContentAuthor) [source: 1]. When an external consumer signs up and pays for a premium tier, you assign their account to that custom App Role [source: 1]. When they log in, Entra External ID embeds that custom role directly inside their JSON Web Token (JWT) claims array ("roles": ["Quiz.PremiumSubscriber"]), allowing your backend code to validate their permissions statelessly [source: 1].

```kotlin
get("/quiz/generate/governance") {
    val userRoles = call.principal<JWTPrincipal>()?.payload?.getClaim("roles")?.asList(String::class.java)
    if (userRoles?.contains("Quiz.PremiumSubscriber") == true) {
        // Serve the advanced SC-300 PIM question fragment
    } else {
        call.respond(HttpStatusCode.Forbidden, "Upgrade required!")
    }
}
```

## Custom domain
1. Register either using TXT or MX to @.
2. MX records route your incoming **emails**, while TXT records provide text-based information used for domain ownership verification, security, and spam prevention.

## Administrator Unit
Just a function to assign roles to specific set of users/groups. Say you have a regional admin, you can use this to assign them to only manage users in their region.

## Company branding
Just branding for login.

## External Collaboration
Not the same.

| Concept | External Collaboration Settings | Microsoft Entra External ID |
| --- | --- | --- |
| **What is it?** | A specific policy configuration blade inside your directory. | The entire product suite/family name for external identities. |
| **Primary Focus** | Controlling B2B Guests entering an enterprise workforce environment. | Managing B2B partners AND building customer-facing (CIAM) apps. |
| **Typical Target User** | A third-party vendor auditor needing access to an internal project. | A retail consumer logging into your web application using Google. |

## User settings - tenant wide
1. User can register app (default true)
2. User can create self service group (default true)
3. Restrict non admin from creating user (default false) -> if true, all user must be created by admin.
4. Linked in account connection (default false)
5. Keep user signed in (default true)

### Exam sample
**Scenario A (Workforce Domain)**: * Question: "Your corporate security officer mandates that employees should not be allowed to share internal SharePoint sites with external consultants using competitor domains. What configuration option should you adjust?"

Architect Answer: Navigate to your standard workforce tenant, open External collaboration settings, and add the competitor's domain name to the Collaboration restrictions / Blocked domains list.

**Scenario B (Consumer Application Domain)**: * Question: "You need to provision an isolated tenant environment to manage millions of mobile app retail customers without letting them interact with your corporate employee lifecycle."

Architect Answer: Create a dedicated customer-facing tenant under the Microsoft Entra External ID umbrella.