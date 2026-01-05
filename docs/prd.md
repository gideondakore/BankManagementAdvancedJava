# Product Requirements Document (PRD)

## Project Title
Bank Account Management System (Advanced Java Lab)

## Version
1.0

## Date
January 05, 2026

## Authors/Contributors
- Lab Developers: IntelliJ IDEA Community Edition (based on technology stack)
- Technology Focus: Java 21 / LTS

## 1. Introduction

### 1.1 Purpose
This Product Requirements Document (PRD) outlines the requirements for enhancing a Bank Account Management System using modern Java features. The system aims to provide efficient, scalable, and thread-safe management of bank accounts and transactions. It builds upon a basic implementation (from Week 2) by migrating to advanced concepts like Java Collections, functional programming, file persistence, regex validation, and concurrency. The primary goal is to create a modular, reusable console-based application that simulates real-world banking operations while demonstrating best practices in Java development.

### 1.2 Scope
- **In Scope**: 
  - Migration from array-based structures to Java Collections (e.g., ArrayList, HashMap).
  - Functional programming integration using Lambdas, Streams, Method References, and Functional Interfaces.
  - File I/O and NIO.Path for persistent storage of accounts and transactions in text files.
  - Regex-based input validation for account numbers, emails, and phone numbers.
  - Thread-safe concurrent transaction processing to simulate multiple deposits/withdrawals.
  - Enhanced console UI with menus, confirmation messages, and real-time thread activity logs.
  - Data processing using streams for filtering, mapping, sorting, and reduction.
- **Out of Scope**:
  - Graphical User Interface (GUI) – Console-based only.
  - Database integration (e.g., SQL) – Focus on file-based persistence for this lab.
  - Network or web features.
  - Advanced security (e.g., encryption beyond basic thread safety).
  - Mobile or cross-platform adaptations.

### 1.3 Target Audience
- Developers/Students learning advanced Java concepts.
- End-users: Simulated bank customers and administrators via console interactions.

### 1.4 Assumptions and Dependencies
- Java 21 / LTS environment with IntelliJ IDEA Community Edition.
- Access to basic text files (accounts.txt, transactions.txt) for data persistence.
- No external libraries beyond standard Java APIs (e.g., java.util, java.nio, java.util.regex).
- System runs in a single JVM instance for concurrency simulation.

## 2. Objectives

By completing this system, users/developers will be able to:
- Use ArrayList and HashMap for efficient account and transaction storage.
- Apply Streams and functional interfaces to perform filtering, mapping, and reduction on collections.
- Apply File I/O (NIO Paths & Files) for saving and loading account data persistently.
- Implement Regex-based validation for customer input formats.
- Demonstrate basic concurrency with threads and synchronized methods during concurrent transactions.
- Maintain thread safety and avoid data corruption.
- Design code that's modular, reusable, and ready for future database integration.

## 3. System Overview

### 3.1 High-Level Architecture
- **Data Management**: In-memory using Collections; persisted to text files.
- **Processing**: Functional streams for operations like loading, filtering, sorting, and transforming data.
- **Validation**: Regex for inputs (account numbers, emails, phones).
- **Concurrency**: Threads with synchronization for safe multi-operation simulations.
- **UI**: Console-based menu-driven interface with confirmation messages and logs.

### 3.2 Key Components
- **Models**: Account, Transaction, SavingsAccount, CheckingAccount, Customer, RegularCustomer, PremiumCustomer.
- **Services**: AccountManager, TransactionManager, FilePersistenceService.
- **Utils**: ValidationUtils, ConcurrencyUtils, FunctionalUtils.
- **Data Files**: accounts.txt, transactions.txt.
- **Docs**: collections-architecture.md, README.md.

### 3.3 Data Flow
- Load data from files on startup using Streams and Method References.
- Process user inputs with validation.
- Perform operations (e.g., search, sort, transactions) using functional programming.
- Save data back to files on exit or updates.
- Simulate concurrent transactions with threads, logging activities.

## 4. Features

### 4.1 Feature 1: Collections Migration with Functional Programming
- Replace arrays with ArrayList and HashMap<String, Account>.
- Implement efficient search, insert, and update operations.
- Support sorting transactions by date or amount using Comparators and Streams.
- Use Lambda expressions for concise iteration and transformation of data.

### 4.2 Feature 2: File Persistence with Functional Stream Processing
- Save all accounts and transactions to files (accounts.txt, transactions.txt).
- Load data automatically on startup.
- Use Java NIO Files and Paths APIs for reading/writing.
- Process loaded data using Streams and Method References to map lines into Account and Transaction objects.

### 4.3 Feature 3: Regex Validation
- Validate account numbers (e.g., ACC[0-9]{3}), emails, and phone numbers.
- Display user-friendly errors for invalid input formats.
- Centralize validation logic in ValidationUtils.
- Optionally, apply Predicate lambdas for dynamic validation rules.

### 4.4 Feature 4: Thread-Safe Concurrent Transactions
- Use Thread and synchronized methods to simulate multiple deposits/withdrawals.
- Demonstrate concurrency by running simultaneous operations safely.
- Prevent race conditions and data inconsistencies.
- Optionally, use parallel streams for batch transaction simulations.

### 4.5 Feature 5: Enhanced Console Experience
- Show data load/save confirmation messages.
- Display thread activities in real time.
- Maintain readable logs for file operations and thread actions.
- Main Menu (Extended) with options like: Manage Accounts, Perform Transactions, Generate Account Statements, Save/Load Data, Run Concurrent Simulation, Exit.

## 5. User Stories

### Epic 1: Collections Migration and Functional Programming
- US-1.1: Replace arrays with ArrayList and HashMap<String, Account> for fast lookup.
- US-1.2: Implement Sorting and Search – Allow sorting transactions by date or amount using Streams and Lambdas.
- Technical Requirements: Use Java Collections API and Streams for filtering, mapping, and sorting.

### Epic 2: File Persistence
- US-2.1: Save Accounts to File – Write account data to accounts.txt on exit.
- US-2.2: Load Accounts on Startup – Read data using Files.lines() and map each line to an object using method references.
- Technical Requirements: Use Java NIO files, Paths, and functional-style processing.

### Epic 3: Regex Validation
- US-3.1: Validate Account Number and Email – Pattern: ACC[0-9]{3}, Email: [A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}.
- Invalid inputs prompt error messages.
- Technical Requirements: Use Pattern, Matcher, and optionally Predicates in ValidationUtils.

### Epic 4: Concurrency and Thread Safety
- US-4.1: Simulate Concurrent Transactions – Run multiple threads executing deposits/withdrawals.
- US-4.2: Ensure Thread Safety – Synchronize critical methods (e.g., updateBalance()).
- Technical Requirements: Use Thread, synchronized, Runnable, or parallel streams.

### Epic 5: Reporting and Persistence Validation
- US-5.1: Generate Statements from File-Loaded Data – Ensure transactions persist between sessions.
- Display data from ArrayList and filter using Streams.

## 6. Functional Requirements

- **Data Persistence**: Auto-load on start, auto-save on updates/exit.
- **Input Validation**: Regex for all user inputs; re-prompt on errors.
- **Transaction Processing**: Support deposit, withdrawal, statements; sort/filter via streams.
- **Concurrency Simulation**: Run at least 3 threads; log operations; verify final balances.
- **Error Handling**: Graceful handling of file I/O exceptions, invalid data.

## 7. Non-Functional Requirements

- **Performance**: Efficient operations (O(1) lookups via HashMap); limit to 20-40 data points for testing.
- **Usability**: Console UI with clear menus and logs; no crashes on invalid inputs.
- **Reliability**: Thread-safe to prevent data corruption; consistent file operations.
- **Maintainability**: Modular code; well-documented (README.md, collections-architecture.md).
- **Compatibility**: Java 21 / LTS; no additional dependencies.
- **Security**: Basic thread safety; no advanced encryption.

## 8. Implementation Phases

### Phase 1: Collections & Functional Migration
- Replace arrays with ArrayList and HashMap.
- Refactor AccountManager and TransactionManager using Streams for filtering and sorting.
- Use Lambdas for concise search and aggregation logic.

### Phase 2: File Persistence
- Implement save/load methods using Files and Paths.
- Use Files.lines() and functional mapping for reading.
- Handle I/O exceptions gracefully.

### Phase 3: Regex Validation
- Add regex checks for emails and account numbers.
- Display helpful error messages.
- Centralize in ValidationUtils.

### Phase 4: Concurrency Integration
- Add threaded or parallel stream-based transaction simulation.
- Mark updateBalance() as synchronized.
- Log thread outputs to console.

## 9. Testing

- **Test Scenario 1**: Collections and Functional Migration – Run the application and perform account and transaction operations; confirm ArrayList and HashMap usage with Streams for search/sort.
- **Test Scenario 2**: File Persistence (Round-Trip) – Create 2 accounts and 3 transactions; save, restart, reload, and verify data integrity.
- **Test Scenario 3**: Regex Validation – Test invalid/valid emails and account numbers; confirm proper error messages.
- **Test Scenario 4**: Concurrent Deposit Simulation – Run "Concurrent Simulation"; verify synchronized deposits and final balance accuracy.
- **Test Scenario 5**: Stream-Based Transaction Sorting – Generate 10 transactions; sort by amount/date using Streams and Lambdas.
- **Additional Tests**: Error handling on file access; persistence with threads; functional reduction (e.g., total deposits); code validation and documentation.

## 10. Grading Rubric

| # | Criteria                  | Points | Excellent (90-100%)                          | Good (75-89%)                          | Satisfactory (60-74%)                  |
|---|---------------------------|--------|----------------------------------------------|----------------------------------------|----------------------------------------|
| 1 | Collections & Functional Migration | 20     | Collections fully implemented with Streams, Lambdas, Functional Interfaces used effectively | Partial integration or inconsistent usage | Minimal or incorrect functional logic |
| 2 | File Persistence          | 20     | Robust file handling with functional-style I/O processing | Partial persistence                   | Minimal file logic                     |
| 3 | Regex Validation          | 15     | Strong patterns and reusable validation     | Works for key fields only              | Limited regex use                      |
| 4 | Concurrency (Threads)     | 15     | Thread-safe logic; optional parallel streams, synchronized methods used | Partial concurrency or missed synchronization | Single-threaded operations             |
| 5 | Functionality & Stability | 10     | All features stable, efficient, and consistent | Minor bugs                             | Unstable logic                         |
| 6 | DSA (Use of Collections & Algorithms) | 10 | Efficient iteration, sorting, and functional algorithms | Logical structure                      | Inefficient loops                      |
| 7 | Documentation             | 10     | Complete README and inline documentation    | Partial coverage                       | Minimal documentation                  |
| 8 | Total                     | 100    |                                              |                                        |                                        |

## 11. Submission Requirements
- Public GitHub Repository with Source Code (/src).
- Data Files (/data).
- README documenting Collections, Functional Programming, File I/O, Regex, and Concurrency.
- Docs (/docs/collections-architecture.md).
- At least one commit reflecting feature progression (collections → functional → I/O → regex → threads).
- Submission Link: Insert Google Form or LMS link here.

## 12. Appendix

### 12.1 Project Structure
```
bank-account-management-system/
├── src/
│   ├── Main.java
│   ├── models/
│   │   ├── Account.java
│   │   ├── SavingsAccount.java
│   │   ├── CheckingAccount.java
│   │   ├── Customer.java
│   │   ├── RegularCustomer.java
│   │   ├── PremiumCustomer.java
│   │   └── Transaction.java
│   ├── services/
│   │   ├── AccountManager.java
│   │   ├── TransactionManager.java
│   │   └── FilePersistenceService.java
│   └── utils/
│       ├── ValidationUtils.java
│       ├── ConcurrencyUtils.java
│       └── FunctionalUtils.java  # New helper for Lambdas and Stream operations
├── data/
│   ├── accounts.txt
│   └── transactions.txt
├── docs/
│   └── collections-architecture.md
└── README.md
```

### 12.2 Expected User Workflows
- **Workflow 1: Data Persistence Cycle** – Start app → auto-loads from files → perform updates → saves updates → confirm success → restart → data persists correctly.
- **Workflow 2: Collections and Functional Migration** – Replace array logic with ArrayList and HashMap → use Streams for improved speed on filtering and sorting.
- **Workflow 3: Regex Validation and Error Handling** – Input invalid account/email → error displayed → re-enter valid data → accepted → verify inputs conform to patterns.
- **Workflow 4: Concurrent Transaction Simulation** – Select "Run Concurrent Simulation" → threads or parallel streams deposit/withdraw simultaneously → observe interleaved logs → verify final balances are accurate and thread-safe.

### 12.3 Risks and Mitigations
- Risk: Data corruption in concurrent operations – Mitigation: Use synchronized methods.
- Risk: File I/O failures – Mitigation: Handle exceptions and provide user feedback.
- Risk: Performance with large data – Mitigation: Keep test data small; use efficient collections.

This PRD serves as a comprehensive guide for developing the Bank Account Management System. For any clarifications, refer to the original project document.