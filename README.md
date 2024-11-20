# 🍜 Aku Butuh Bakso !
Udah sore, udara dingin, enaknya makan **bakso**. 

> Mang **bakso** nya mana ya?

Aplikasi ini membantu penjual atau calon pembeli bakso untuk saling menemukan satu sama lain!


# How To Run
## Google Maps Config

To add your Maps API key to this project:
1.  If the secrets.properties file does not exist, create it in the same folder as the local.properties file.
2.  Add this line, where "YOUR_API_KEY" is your API key: MAPS_API_KEY=YOUR_API_KEY

```mermaid
sequenceDiagram
actor user as User

    Title: Login Flow
    user ->> app: Input username, role, check T&C
    app ->> app: Validate inputs
    app ->> user: Request location permission
    alt Location permission granted
        user ->> app: Grant permission
        app ->> user: Show Map Screen
    else Location permission denied
        user ->> app: Deny permission
        app ->> user: Show rationale dialog
    end
```
```mermaid
sequenceDiagram
actor user as User

    Title: Map Flow
    app ->> db: Add user to Firebase
    alt User is Seller
        app ->> db: Fetch and listen all Buyer locations
    else User is Buyer
        app ->> db: Fetch and listen all Seller locations
    end
    db ->> app: Return all online user locations by role
    app ->> user: Display user locations on map

    
```

```mermaid
sequenceDiagram
actor user as User

    Title: Live Location Sharing
    alt User is Seller
        user ->> app: Update location (real-time)
        app ->> db: Update location in Firebase
    else User is Buyer
        db ->> user: Return real-time Seller location
    end
```

```mermaid
sequenceDiagram
actor user as User

    Title: Closing Map Screen
    user ->> app: Close Map Screen (click or move to background)
    app ->> db: Delete user location
```
