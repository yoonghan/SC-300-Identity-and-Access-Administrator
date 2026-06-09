# Condition Access

This is Entra condition access, which is to protect user/tenant login.

## Common Settings
1. Need to disable "**Security Default**" for tenant, then choose using "Conditional Access". (Identity > Protect & secure > Conditional Access > Conditional Access)
2. Requires a P1 or P2 license.
3. [link](https://learn.microsoft.com/en-us/entra/fundamentals/security-defaults#enabling-security-defaults)

## Terms of Use
1. Create terms of use and force accept every login.
2. You can:
    a. Create a custom policy
    b. Require users to accept terms of use, e.g., accept every 1 days, annually or forever.