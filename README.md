# Telecom Billing System 🚀

A modular Telecom Billing System built with a Java-based backend and a reactive frontend.

---

## 🏗️ Architecture Stack
- **Backend**: Java (Jakarta EE 11) on Apache Tomcat 11.
- **Frontend**: SvelteKit 5 (Static Adapter) featuring an **AMOLED Obsidian** dark-mode interface.
- **Database**: PostgreSQL (**Neon DB**) with HikariCP connection pooling.
- **Reporting**: **JasperReports 7.0.4** (element kind schema) with JIT Caching.
- **Security**: Unified `AppFilter` for session management and path normalization.

---

## ✨ Key Features
- **Administrative Control Panel**: A central dashboard for managing the telecom lifecycle: Customers, Contracts, Service Packages, and Rateplans.
- **CDR Engine**: A Java-based parser (`CDRParser`) that validates, rates, and transforms raw CSV data into financial records.
- **Jasper 7 Invoicing**: PDF generation using the **7.0.4 element-kind schema**, featuring custom SVG iconography.
- **Automated Billing**: Server-side logic for 10% tax calculation, recurring service assignment, and one-time fee processing.
- **SPA Path Resilience**: A robust `AppFilter` that manages path normalization for SvelteKit client-side routing.
- **Unified Security Model**: Multi-layered authentication with session-based enforcement and HTTP-only cookie security.
- **Cloud Database (NeonDB)**: Distributed PostgreSQL architecture with **HikariCP** for connection pooling.

---

## 🚀 Deployment & Execution

### 1. Development (IDE Mode - IntelliJ / NetBeans)
The system is "IDE-Aware" and will automatically detect its environment.
1.  **Clone** the repository.
2.  Set up your **NeonDB** credentials in `src/main/resources/db.properties`.
3.  **Build** the project: `./mvnw clean package`.
4.  **Run** `com.billing.Main` class directly.
*The engine will detect it is in an IDE and serve assets from `src/main/webapp`.*

### 2. Production (Container Mode - Podman)
This is the **Hardened Deployment** recommended for production.
1.  **Configure Secrets**:
    ```bash
    cp .env.example .env     # Add your NeonDB URL, User, and Password
    ```
2.  **Launch with Podman-Compose**:
    ```bash
    podman-compose up -d --build
    ```
3.  **Link Nginx Armor**:
    ```bash
    # Sync the hardened proxy config to your host's Nginx
    sudo cp deploy/nginx.conf /etc/nginx/conf.d/billing.conf
    sudo nginx -s reload
    ```

---

## 🔄 System Core & Data Flow

Understanding the lifecycle of a transaction in FMRZ:

1.  **Ingestion Phase**: Raw CDR files are placed in the `input/` directory.
2.  **Rating Phase**: The `AdminCDRServlet` triggers the `CDRParser`. 
3.  **Persistence Phase**: Rated records are committed to the `cdr` table, and the raw file is archived.
4.  **Billing Cycle**: Aggregates usage, applies services, and calculates **10% Government Tax**.
5.  **Rendering Phase**: `CustomerProfileServlet` generates Jasper-rendered PDFs using cached templates.

---

## 🛠️ Infrastructure & DevOps (Containerized Architecture)

The system has been upgraded to a **Hardened Standalone JAR** architecture, protected by an **Nginx Edge Gateway** and managed via **Podman Orchestration**.

### 1. The "Nginx Armor" (Synchronized Proxy)
The application is protected by Nginx, which acts as a high-performance security shield:
- **Unified Proxying**: Nginx proxies all traffic (including static assets) to the container. This ensures the UI JavaScript and the Backend API are **always in sync**, preventing 404 errors.
- **Security Headers**: Injects `X-Frame-Options`, `X-Content-Type-Options`, and `Gzip` compression at the edge.
- **Domain Mapping**: Configured to serve the project via `http://billing.local`.

### 2. Standalone Engine (Self-Detecting JAR)
The system compiles into a single executable: **`Telecom-Billing-Engine.jar`**.
- **Dynamic Resource Mapping**: The code automatically detects its environment and adjusts its resource scanning (`JarResourceSet`).
- **Embedded Tomcat 11**: Self-contained server with optimized connector threads.

### 3. Container Orchestration (Podman)
The project is fully containerized using a **Multi-Stage Dockerfile**.
- **Build Stage**: Compiles Java 21 and SvelteKit 5 inside a clean, isolated environment.
- **Runtime Stage**: Uses a lightweight JRE and a **non-root user** (`billinguser`) to prevent system-level attacks.
- **Environment Driven**: Database credentials are injected at runtime via environment variables.

---

## 🛡️ Security Hardening Audit (April 2026)
The system has been patched against critical vulnerabilities and follows enterprise hardening standards:
| Component | Status | Fix Description |
| :--- | :--- | :--- |
| **Identity** | ✅ Hardened | Running as non-root `billinguser` to prevent container escape. |
| **Secrets** | ✅ Secured | Removed all passwords from source code; moved to `.env`. |
| **Assets** | ✅ Synchronized | Assets served from container to prevent desync 404s. |
| **Reporting** | ✅ Optimized | JIT Caching implemented for JasperReports to prevent DoS. |
| **Nginx** | ✅ Shielded | Global proxying for Atomic UI/API updates. |

---

*This architecture ensures that the FMRZ Telecom Billing System is secure, portable, and ready for high-scale production loads.* 🏎️🛡️🚀