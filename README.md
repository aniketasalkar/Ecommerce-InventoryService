# Ecommerce-InventoryService

Inventory Service for Ecommerce Application

## API Documentation

### InventoryController

#### Create Inventory Item
- **Endpoint:** `/api/inventory/inventory_item/create`
- **Method:** `POST`
- **Description:** Creates a new inventory item.
- **Request Body:** `InventoryItemRequestDto`
- **Response:**
  - `201 Created`: Returns the created inventory item details.
  - `400 Bad Request`: If the request is invalid.
  - `500 Internal Server Error`: If there is an error during creation.

#### Get Inventory Item
- **Endpoint:** `/api/inventory/inventory_item/get/{product_id}`
- **Method:** `GET`
- **Description:** Retrieves an inventory item by product ID.
- **Path Variable:** `product_id`
- **Response:**
  - `200 OK`: Returns the inventory item details.
  - `404 Not Found`: If the product ID is not found.
  - `500 Internal Server Error`: If there is an error during retrieval.

#### Update Inventory Item
- **Endpoint:** `/api/inventory/inventory_item/update/{product_id}`
- **Method:** `PATCH`
- **Description:** Updates an inventory item by product ID.
- **Path Variable:** `product_id`
- **Request Body:** `InventoryItemPatchRequestDto`
- **Response:**
  - `200 OK`: Returns the updated inventory item details.
  - `400 Bad Request`: If the request is invalid.
  - `404 Not Found`: If the product ID is not found.
  - `500 Internal Server Error`: If there is an error during update.

#### Add Quantity to Inventory
- **Endpoint:** `/api/inventory/inventory_item/addQuantity/{product_id}`
- **Method:** `PATCH`
- **Description:** Adds quantity to an inventory item by product ID.
- **Path Variable:** `product_id`
- **Request Body:** `AddQuantityDto`
- **Response:**
  - `200 OK`: Returns the updated inventory item details with added quantity.
  - `400 Bad Request`: If the request is invalid.
  - `404 Not Found`: If the product ID is not found.
  - `500 Internal Server Error`: If there is an error during quantity addition.

### InventoryReservationController

#### Reserve Inventory Item
- **Endpoint:** `/api/inventory/reservation/reserve`
- **Method:** `POST`
- **Description:** Reserves an inventory item.
- **Request Body:** `InventoryReservationRequestDto`
- **Response:**
  - `201 Created`: Returns the reservation details.
  - `400 Bad Request`: If the request is invalid.
  - `500 Internal Server Error`: If there is an error during reservation.

#### Revoke Inventory Reservation
- **Endpoint:** `/api/inventory/reservation/revoke`
- **Method:** `POST`
- **Description:** Revokes an inventory reservation.
- **Request Body:** `RevokeReservationDto`
- **Response:**
  - `200 OK`: Returns the revocation details.
  - `400 Bad Request`: If the request is invalid.
  - `500 Internal Server Error`: If there is an error during revocation.
