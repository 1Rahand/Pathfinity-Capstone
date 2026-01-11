# Pathfinity: Integrated Educational & Career Development Ecosystem

**Senior Capstone Project | American University of Iraq, Sulaimani (2024-2025)**

## Project Details
- **iOS (TestFlight):** [Pathfinity TestFlight](https://testflight.apple.com/join/NTuEhcQf) (Note: Hosted via peer account)
- **Android (APK):** [Pathfinity apk](https://drive.google.com/drive/folders/13M3tkB-aEOw_geXsNe1W_U2a7CbvkOlb?usp=sharing)
- **Capstone Poster:** [Capstone_Poster](https://drive.google.com/file/d/1gWhi9EWm3yng014khCVK7Inj-7JEFqx1/view?usp=sharing)
- **Project Link:** [Pathfinity-Capstone](https://github.com/1Rahand/Pathfinity-Capstone)

## Project Overview
Pathfinity addresses the **"Practical Skills Gap"** in higher education by creating a digital ecosystem that bridges the disconnect between academic learning and industry requirements. The system connects Students, Alumni, Content Creators, and Corporate Recruiters through two distinct applications working in tandem.

## System Architecture
The solution relies on a split-client architecture allowing for real-time interaction between four distinct user roles:
1. **Students:** Access to courses, internships, and mentorship.
2. **Alumni:** Professional networking and mentorship chat functionality.
3. **Creators:** Live-streaming tools for educational workshops.
4. **Companies:** Recruitment portal for posting internships and reviewing applicants.

## Tech Stack
- **Student Client:** Built with **Kotlin** (Compose Multiplatform) for modern, reactive user experience.
- **Admin & Multi-Role Portal:** Built with **Flutter** (Dart) for cross-platform manageability.
- **Backend:** Supabase (PostgreSQL, Auth, Realtime).

## Live Streaming Infrastructure
To support high-quality interactive workshops, the platform integrates **Agora.io** for real-time broadcasting.
- **Video Engine:** Utilizes the Agora RTC SDK to ensure low-latency video and audio streaming between Creators (Web) and Students (Mobile).
- **Security Middleware:** A custom **Python (Flask)** server handles token generation, ensuring that only authenticated users with the correct RBAC permissions can publish or subscribe to streams.
- **Availability Note:** The live streaming feature is currently **exclusive to the Android Client**. It is not available in the iOS (TestFlight) build.

## Contributors & Roles

### Shakar Mustafa Ali - *Flutter Developer & Feature Lead*
- **Cross-Platform Development:** Built the comprehensive Admin & Multi-Role portal using Flutter to support complex workflows.
- **Live Streaming Engine:** Engineered the real-time broadcasting feature allowing Creators to stream workshops directly to students.
- **Backend Connectivity:** Handled API integration with Supabase for the Admin side, ensuring data consistency across the multi-role dashboard.
- **State Management:** Implemented complex state management to handle simultaneous sessions for Alumni and Companies.

### Rahand Omed Noori - *Project Lead & System Integrator*
- **Concept & Architecture:** Originated the project concept and designed the dual-app system architecture.
- **Integration:** Managed the critical data synchronization between the Kotlin (Student) and Flutter (Admin) clients.
- **Core Logic:** Implemented the Role-Based Access Control (RBAC) logic for the 4 distinct user types in the Admin Panel.
- **QA & Testing:** Conducted end-to-end system testing and quality assurance to verify data integrity across platforms.
- **Management:** Led sprint planning and task distribution for the team.

### Enos Salar Azwar - *Mobile Developer (Kotlin) & UX/UI Lead*
- **Native Android/Multiplatform Development:** Built the high-performance Student Client using **Kotlin** and **Compose Multiplatform**.
- **User Experience Design:** Led the prototyping, wireframing, and visual design for the entire ecosystem (both apps).
- **Student Workflows:** Implemented the core student features including course navigation, internship application forms, and profile management.
- **Interface Standards:** Ensured the mobile client adhered to modern Material Design guidelines for maximum usability.

## Key Features Implemented
- **Role-Based Access Control (RBAC):** Secure authentication flows distinguishing between 4 distinct user types.
- **Real-time Chat & Messaging:** Instant messaging for mentorship and live data updates.
- **Live Streaming Integration:** Broadcasting tools for educational content.
- **Internship Workflow:** End-to-end application and review system.
- **Course Management:** Uploading and consuming educational video content.
