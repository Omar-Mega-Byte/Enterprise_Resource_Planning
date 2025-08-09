# Changelog

## [v1.4.0] - 2025-08-09
### Added
- Global exception handler for the inventory module.
- `InventoryDashboardController` and `ProductController` for inventory management APIs.
- Security configuration with basic authentication and endpoint filtering.

### Changed
- Refactored product sorting logic using a switch expression for improved readability.

---

## [v1.3.0] - 2025-08-05
### Added
- Product service with CRUD and search functionality.
- DTOs for product creation, update, response, and summary.
- Product validation logic and custom exceptions.
- SKU generator and validation.

---

## [v1.2.0] - 2025-07-29
### Added
- Department, Employee, and Job Title management modules with CRUD operations.
- Validation logic for HR entities.
- Global exception handling for HR services.

### Changed
- Refactored security configuration for HR endpoints.

---

## [v1.1.0] - 2025-07-20
### Added
- Initial project structure with Maven, Docker config, and base entities.
- Basic security configuration.
- Core HR entities (`Employee`, `Department`, `JobTitle`, `Gender`).

