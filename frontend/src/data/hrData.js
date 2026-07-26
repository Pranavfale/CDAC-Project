export const hrStats = [
  {
    id: 1,
    title: "Active Jobs",
    value: 12,
    description: "Currently accepting applications",
    type: "jobs",
  },
  {
    id: 2,
    title: "Total Applications",
    value: 248,
    description: "Across all active vacancies",
    type: "applications",
  },
  {
    id: 3,
    title: "Shortlisted",
    value: 46,
    description: "Candidates moved to next stage",
    type: "shortlisted",
  },
  {
    id: 4,
    title: "Interviews",
    value: 18,
    description: "Scheduled and upcoming",
    type: "interviews",
  },
];

export const hiringPipeline = [
  {
    stage: "Applied",
    count: 248,
  },
  {
    stage: "Under Review",
    count: 126,
  },
  {
    stage: "Shortlisted",
    count: 46,
  },
  {
    stage: "Interview",
    count: 18,
  },
  {
    stage: "Selected",
    count: 8,
  },
  {
    stage: "Offered",
    count: 6,
  },
  {
    stage: "Hired",
    count: 4,
  },
];

export const hrRecentApplications = [
  {
    id: 601,
    candidate: "Abhijeet Arekar",
    initials: "AA",
    position: "Java Developer",
    department: "Software Development",
    appliedDate: "24 Jul 2026",
    experience: "1 Year",
    status: "UNDER_REVIEW",
  },
  {
    id: 602,
    candidate: "Pranav Fale",
    initials: "PP",
    position: "Frontend React Developer",
    department: "Software Development",
    appliedDate: "24 Jul 2026",
    experience: "2 Years",
    status: "SHORTLISTED",
  },
  {
    id: 603,
    candidate: "Akash Battula",
    initials: "AB",
    position: "Backend Developer",
    department: "Engineering",
    appliedDate: "23 Jul 2026",
    experience: "Fresher",
    status: "APPLIED",
  },
  {
    id: 604,
    candidate: "Siddharth Rukadikar",
    initials: "SR",
    position: "Software Engineer Trainee",
    department: "Engineering",
    appliedDate: "23 Jul 2026",
    experience: "Fresher",
    status: "UNDER_REVIEW",
  },
  {
    id: 605,
    candidate: "Aditya Joshi",
    initials: "AJ",
    position: "Associate Software Developer",
    department: "Engineering",
    appliedDate: "22 Jul 2026",
    experience: "1 Year",
    status: "INTERVIEW_SCHEDULED",
  },
];

export const hrUpcomingInterviews = [
  {
    id: 701,
    candidate: "Abhijeet Arekar",
    position: "Frontend React Developer",
    round: "TECHNICAL",
    date: "25 Jul 2026",
    time: "10:30 AM",
    mode: "ONLINE",
  },
  {
    id: 702,
    candidate: "Pranav Fale",
    position: "Java Developer",
    round: "TECHNICAL",
    date: "26 Jul 2026",
    time: "11:00 AM",
    mode: "ONLINE",
  },
  {
    id: 703,
    candidate: "Akash Battula",
    position: "Associate Software Developer",
    round: "HR",
    date: "26 Jul 2026",
    time: "03:00 PM",
    mode: "OFFLINE",
  },
];

export const hrActiveJobs = [
  {
    id: 801,
    title: "Java Developer",
    department: "Software Development",
    applications: 68,
    openings: 4,
    status: "ACTIVE",
  },
  {
    id: 802,
    title: "Frontend React Developer",
    department: "Software Development",
    applications: 52,
    openings: 3,
    status: "ACTIVE",
  },
  {
    id: 803,
    title: "Backend Developer",
    department: "Engineering",
    applications: 44,
    openings: 2,
    status: "ACTIVE",
  },
  {
    id: 804,
    title: "Software Engineer Trainee",
    department: "Engineering",
    applications: 39,
    openings: 5,
    status: "ACTIVE",
  },
];

export const hrJobs = [
  {
    id: 801,
    title: "Java Developer",
    department: "Software Development",
    location: "Pune, Maharashtra",
    employmentType: "FULL TIME",
    workMode: "HYBRID",
    experience: "1 - 3 Years",
    openings: 4,
    applications: 68,
    salary: "₹5,00,000 - ₹8,00,000",
    postedDate: "15 Jul 2026",
    closingDate: "31 Jul 2026",
    status: "ACTIVE",

    description:
      "We are looking for a Java Developer to design, develop and maintain scalable enterprise web applications.",

    responsibilities: [
      "Develop backend services using Java and Spring Boot",
      "Design and consume RESTful APIs",
      "Work with relational databases",
      "Collaborate with frontend and QA teams",
      "Write clean and maintainable code",
    ],

    skills: ["Java", "Spring Boot", "REST API", "MySQL", "Git"],
  },

  {
    id: 802,
    title: "Frontend React Developer",
    department: "Software Development",
    location: "Mumbai, Maharashtra",
    employmentType: "FULL TIME",
    workMode: "HYBRID",
    experience: "1 - 3 Years",
    openings: 3,
    applications: 52,
    salary: "₹5,50,000 - ₹9,00,000",
    postedDate: "16 Jul 2026",
    closingDate: "02 Aug 2026",
    status: "ACTIVE",

    description:
      "We are hiring a React Developer to build responsive and reusable frontend applications.",

    responsibilities: [
      "Develop reusable React components",
      "Integrate frontend applications with REST APIs",
      "Implement responsive user interfaces",
      "Manage client-side application state",
      "Collaborate with backend developers",
    ],

    skills: ["React", "JavaScript", "HTML", "CSS", "REST API"],
  },

  {
    id: 803,
    title: "Backend Developer",
    department: "Engineering",
    location: "Hyderabad, Telangana",
    employmentType: "FULL TIME",
    workMode: "REMOTE",
    experience: "2 - 4 Years",
    openings: 2,
    applications: 44,
    salary: "₹7,00,000 - ₹11,00,000",
    postedDate: "18 Jul 2026",
    closingDate: "05 Aug 2026",
    status: "ACTIVE",

    description:
      "We are looking for a Backend Developer to build secure and scalable backend systems.",

    responsibilities: [
      "Develop RESTful backend services",
      "Design database schemas",
      "Implement authentication and authorization",
      "Optimize application performance",
      "Participate in code reviews",
    ],

    skills: ["Java", "Spring Boot", "MySQL", "REST API", "Spring Security"],
  },

  {
    id: 804,
    title: "Software Engineer Trainee",
    department: "Engineering",
    location: "Pune, Maharashtra",
    employmentType: "FULL TIME",
    workMode: "OFFICE",
    experience: "Fresher",
    openings: 5,
    applications: 39,
    salary: "₹4,00,000 - ₹5,00,000",
    postedDate: "20 Jul 2026",
    closingDate: "10 Aug 2026",
    status: "ACTIVE",

    description:
      "An entry-level opportunity for graduates interested in software development and enterprise applications.",

    responsibilities: [
      "Participate in application development",
      "Fix defects and perform testing",
      "Learn project technologies",
      "Assist senior developers",
      "Participate in technical training",
    ],

    skills: ["Java", "SQL", "HTML", "CSS", "JavaScript"],
  },

  {
    id: 805,
    title: "QA Engineer",
    department: "Quality Assurance",
    location: "Pune, Maharashtra",
    employmentType: "FULL TIME",
    workMode: "OFFICE",
    experience: "1 - 2 Years",
    openings: 2,
    applications: 21,
    salary: "₹4,50,000 - ₹6,50,000",
    postedDate: "10 Jul 2026",
    closingDate: "25 Jul 2026",
    status: "CLOSED",

    description:
      "Responsible for ensuring software quality through manual and automated testing.",

    responsibilities: [
      "Prepare test cases",
      "Execute functional testing",
      "Report and track defects",
      "Perform regression testing",
      "Work closely with development teams",
    ],

    skills: ["Manual Testing", "Selenium", "API Testing", "SQL"],
  },

  {
    id: 806,
    title: "DevOps Engineer",
    department: "Infrastructure",
    location: "Bengaluru, Karnataka",
    employmentType: "FULL TIME",
    workMode: "HYBRID",
    experience: "2 - 4 Years",
    openings: 2,
    applications: 0,
    salary: "₹8,00,000 - ₹12,00,000",
    postedDate: "Not Published",
    closingDate: "15 Aug 2026",
    status: "DRAFT",

    description:
      "We are preparing a vacancy for a DevOps Engineer to manage CI/CD and cloud infrastructure.",

    responsibilities: [
      "Maintain CI/CD pipelines",
      "Manage Docker-based deployments",
      "Monitor infrastructure",
      "Automate deployment processes",
      "Collaborate with development teams",
    ],

    skills: ["Docker", "Git", "Linux", "CI/CD", "Cloud"],
  },
];

export const hrApplications = [
  {
    id: 601,
    candidateId: 501,
    candidate: "Abhijeet Arekar",
    initials: "AA",
    email: "abhijeetarekar@gmail.com",
    phone: "+91 98765 43210",
    location: "Pune, Maharashtra",

    position: "Java Developer",
    jobId: 801,
    department: "Software Development",

    experience: "1 Year",
    appliedDate: "24 Jul 2026",
    status: "UNDER_REVIEW",

    headline: "Java Full Stack Developer",

    summary:
      "Motivated software developer with strong fundamentals in Java, Spring Boot, React, SQL and Data Structures. Interested in building scalable web applications.",

    skills: [
      "Java",
      "Spring Boot",
      "React",
      "JavaScript",
      "MySQL",
      "REST API",
      "Git",
    ],

    education: [
      {
        id: 1,
        degree: "Bachelor of Technology",
        specialization: "Computer Science & Engineering",
        institute: "ABC Institute of Technology",
        year: "2019 - 2023",
        score: "8.2 CGPA",
      },
      {
        id: 2,
        degree: "Post Graduate Diploma",
        specialization: "Advanced Computing",
        institute: "C-DAC Training Centre",
        year: "2026",
        score: "Pursuing",
      },
    ],

    resume: "Abhijeet_Arekar_Resume.pdf",
  },

  {
    id: 602,
    candidateId: 502,
    candidate: "Akash Battula",
    initials: "AB",
    email: "akashbattula@gmail.com",
    phone: "+91 98765 11111",
    location: "Mumbai, Maharashtra",

    position: "Frontend React Developer",
    jobId: 802,
    department: "Software Development",

    experience: "2 Years",
    appliedDate: "24 Jul 2026",
    status: "SHORTLISTED",

    headline: "Frontend Developer",

    summary:
      "Frontend developer experienced in React, JavaScript and responsive web application development.",

    skills: ["React", "JavaScript", "HTML", "CSS", "Redux", "REST API"],

    education: [
      {
        id: 1,
        degree: "Bachelor of Engineering",
        specialization: "Information Technology",
        institute: "XYZ College of Engineering",
        year: "2019 - 2023",
        score: "8.5 CGPA",
      },
    ],

    resume: "Akash_Battula_Resume.pdf",
  },

  {
    id: 603,
    candidateId: 503,
    candidate: "Pranav Fale",
    initials: "PF",
    email: "pranavfale@gmail.com",
    phone: "+91 98765 22222",
    location: "Pune, Maharashtra",

    position: "Backend Developer",
    jobId: 803,
    department: "Engineering",

    experience: "Fresher",
    appliedDate: "23 Jul 2026",
    status: "APPLIED",

    headline: "Backend Developer",

    summary:
      "Entry-level developer interested in Java backend development, databases and REST APIs.",

    skills: ["Java", "Spring Boot", "SQL", "MySQL", "Git"],

    education: [
      {
        id: 1,
        degree: "Bachelor of Technology",
        specialization: "Computer Engineering",
        institute: "Maharashtra Institute of Technology",
        year: "2020 - 2024",
        score: "7.9 CGPA",
      },
    ],

    resume: "Pranav_Fale_Resume.pdf",
  },

  {
    id: 604,
    candidateId: 504,
    candidate: "Siddharth Rukadikar",
    initials: "SR",
    email: "siddharth@gmail.com",
    phone: "+91 98765 33333",
    location: "Nashik, Maharashtra",

    position: "Software Engineer Trainee",
    jobId: 804,
    department: "Engineering",

    experience: "Fresher",
    appliedDate: "23 Jul 2026",
    status: "UNDER_REVIEW",

    headline: "Software Developer",

    summary:
      "Graduate software developer with knowledge of Java, SQL and frontend technologies.",

    skills: ["Java", "SQL", "HTML", "CSS", "JavaScript"],

    education: [
      {
        id: 1,
        degree: "Bachelor of Engineering",
        specialization: "Computer Science",
        institute: "Engineering College Nashik",
        year: "2020 - 2024",
        score: "8.1 CGPA",
      },
    ],

    resume: "Siddharth_Rukadikar_Resume.pdf",
  },

  {
    id: 605,
    candidateId: 505,
    candidate: "Aditya Joshi",
    initials: "AJ",
    email: "aditya.joshi@gmail.com",
    phone: "+91 98765 44444",
    location: "Pune, Maharashtra",

    position: "Associate Software Developer",
    jobId: 807,
    department: "Engineering",

    experience: "1 Year",
    appliedDate: "22 Jul 2026",
    status: "INTERVIEW_SCHEDULED",

    headline: "Associate Software Developer",

    summary:
      "Software developer with experience in Java application development and relational databases.",

    skills: ["Java", "Spring Boot", "MySQL", "JavaScript", "Git"],

    education: [
      {
        id: 1,
        degree: "Bachelor of Technology",
        specialization: "Information Technology",
        institute: "Pune Institute of Technology",
        year: "2019 - 2023",
        score: "8.0 CGPA",
      },
    ],

    resume: "Aditya_Joshi_Resume.pdf",
  },

  {
    id: 606,
    candidateId: 506,
    candidate: "Neha More",
    initials: "NM",
    email: "neha.more@gmail.com",
    phone: "+91 98765 55555",
    location: "Mumbai, Maharashtra",

    position: "Java Developer",
    jobId: 801,
    department: "Software Development",

    experience: "2 Years",
    appliedDate: "21 Jul 2026",
    status: "REJECTED",

    headline: "Java Developer",

    summary:
      "Java developer with experience in backend application development.",

    skills: ["Java", "Spring Boot", "SQL", "REST API"],

    education: [
      {
        id: 1,
        degree: "Bachelor of Engineering",
        specialization: "Computer Engineering",
        institute: "Mumbai Engineering College",
        year: "2018 - 2022",
        score: "7.6 CGPA",
      },
    ],

    resume: "Neha_More_Resume.pdf",
  },
];

export const hrInterviews = [
  {
    id: 701,
    applicationId: 602,
    candidateId: 502,

    candidate: "Abhijeet Arekar",
    initials: "AA",
    email: "abhijeetarekar@gmail.com",

    position: "Frontend React Developer",
    department: "Software Development",

    round: "Technical Round 1",
    interviewDate: "25 Jul 2026",
    interviewTime: "11:00 AM",
    duration: "45 Minutes",

    mode: "ONLINE",
    interviewer: "Rahul Mehta",
    meetingLink: "Google Meet",

    status: "SCHEDULED",
  },

  {
    id: 702,
    applicationId: 605,
    candidateId: 505,

    candidate: "Pranav Fale",
    initials: "PF",
    email: "pranavfale@gmail.com",

    position: "Associate Software Developer",
    department: "Engineering",

    round: "Technical Round 1",
    interviewDate: "25 Jul 2026",
    interviewTime: "02:30 PM",
    duration: "60 Minutes",

    mode: "ONLINE",
    interviewer: "Snehal Patil",
    meetingLink: "Microsoft Teams",

    status: "SCHEDULED",
  },

  {
    id: 703,
    applicationId: 610,
    candidateId: 510,

    candidate: "Akash Battula",
    initials: "AB",
    email: "akashbattula@gmail.com",

    position: "Java Developer",
    department: "Software Development",

    round: "HR Round",
    interviewDate: "24 Jul 2026",
    interviewTime: "04:00 PM",
    duration: "30 Minutes",

    mode: "OFFLINE",
    interviewer: "Anjali Deshmukh",
    meetingLink: "Conference Room A",

    status: "COMPLETED",
  },

  {
    id: 704,
    applicationId: 611,
    candidateId: 511,

    candidate: "Siddharth Rukadikar",
    initials: "SR",
    email: "siddharth@gmail.com",

    position: "Backend Developer",
    department: "Engineering",

    round: "Technical Round 2",
    interviewDate: "26 Jul 2026",
    interviewTime: "10:30 AM",
    duration: "60 Minutes",

    mode: "OFFLINE",
    interviewer: "Vikram Joshi",
    meetingLink: "Interview Room 2",

    status: "SCHEDULED",
  },
];

export const hrCandidates = [
  {
    id: 501,
    name: "Abhijeet",
    initials: "AA",
    email: "abhijeet@gmail.com",
    phone: "+91 98765 43210",
    location: "Pune, Maharashtra",

    headline: "Java Full Stack Developer",
    experience: "1 Year",
    status: "ACTIVE",
    joinedDate: "20 Jul 2026",

    summary:
      "Motivated software developer with strong fundamentals in Java, Spring Boot, React, SQL and Data Structures.",

    skills: [
      "Java",
      "Spring Boot",
      "React",
      "JavaScript",
      "MySQL",
      "REST API",
      "Git",
    ],

    education: [
      {
        id: 1,
        degree: "Bachelor of Technology",
        specialization: "Computer Science & Engineering",
        institute: "ABC Institute of Technology",
        year: "2019 - 2023",
        score: "8.2 CGPA",
      },
      {
        id: 2,
        degree: "Post Graduate Diploma",
        specialization: "Advanced Computing",
        institute: "C-DAC Training Centre",
        year: "2026",
        score: "Pursuing",
      },
    ],

    applications: [
      {
        id: 601,
        position: "Java Developer",
        appliedDate: "24 Jul 2026",
        status: "UNDER_REVIEW",
      },
    ],
  },

  {
    id: 502,
    name: "Akash",
    initials: "AB",
    email: "akashbattula@gmail.com",
    phone: "+91 98765 11111",
    location: "Mumbai, Maharashtra",

    headline: "Frontend React Developer",
    experience: "2 Years",
    status: "ACTIVE",
    joinedDate: "18 Jul 2026",

    summary:
      "Frontend developer experienced in React, JavaScript, Redux and responsive web application development.",

    skills: ["React", "JavaScript", "HTML", "CSS", "Redux", "REST API"],

    education: [
      {
        id: 1,
        degree: "Bachelor of Engineering",
        specialization: "Information Technology",
        institute: "XYZ College of Engineering",
        year: "2019 - 2023",
        score: "8.5 CGPA",
      },
    ],

    applications: [
      {
        id: 602,
        position: "Frontend React Developer",
        appliedDate: "24 Jul 2026",
        status: "SHORTLISTED",
      },
    ],
  },

  {
    id: 503,
    name: "Siddharth",
    initials: "SR",
    email: "siddharth@gmail.com",
    phone: "+91 98765 22222",
    location: "Pune, Maharashtra",

    headline: "Backend Developer",
    experience: "Fresher",
    status: "ACTIVE",
    joinedDate: "17 Jul 2026",

    summary:
      "Entry-level developer interested in Java backend development, relational databases and REST APIs.",

    skills: ["Java", "Spring Boot", "SQL", "MySQL", "Git"],

    education: [
      {
        id: 1,
        degree: "Bachelor of Technology",
        specialization: "Computer Engineering",
        institute: "Maharashtra Institute of Technology",
        year: "2020 - 2024",
        score: "7.9 CGPA",
      },
    ],

    applications: [
      {
        id: 603,
        position: "Backend Developer",
        appliedDate: "23 Jul 2026",
        status: "APPLIED",
      },
    ],
  },

  {
    id: 504,
    name: "Pranav",
    initials: "PF",
    email: "pranav@gmail.com",
    phone: "+91 98765 33333",
    location: "Nashik, Maharashtra",

    headline: "Software Developer",
    experience: "Fresher",
    status: "ACTIVE",
    joinedDate: "15 Jul 2026",

    summary:
      "Graduate software developer with knowledge of Java, SQL and frontend technologies.",

    skills: ["Java", "SQL", "HTML", "CSS", "JavaScript"],

    education: [
      {
        id: 1,
        degree: "Bachelor of Engineering",
        specialization: "Computer Science",
        institute: "Engineering College Nashik",
        year: "2020 - 2024",
        score: "8.1 CGPA",
      },
    ],

    applications: [
      {
        id: 604,
        position: "Software Engineer Trainee",
        appliedDate: "23 Jul 2026",
        status: "UNDER_REVIEW",
      },
    ],
  },

  {
    id: 505,
    name: "Aditya Joshi",
    initials: "AJ",
    email: "aditya.joshi@gmail.com",
    phone: "+91 98765 44444",
    location: "Pune, Maharashtra",

    headline: "Associate Software Developer",
    experience: "1 Year",
    status: "ACTIVE",
    joinedDate: "14 Jul 2026",

    summary:
      "Software developer with experience in Java application development and relational databases.",

    skills: ["Java", "Spring Boot", "MySQL", "JavaScript", "Git"],

    education: [
      {
        id: 1,
        degree: "Bachelor of Technology",
        specialization: "Information Technology",
        institute: "Pune Institute of Technology",
        year: "2019 - 2023",
        score: "8.0 CGPA",
      },
    ],

    applications: [
      {
        id: 605,
        position: "Associate Software Developer",
        appliedDate: "22 Jul 2026",
        status: "INTERVIEW_SCHEDULED",
      },
    ],
  },

  {
    id: 506,
    name: "Neha More",
    initials: "NM",
    email: "neha.more@gmail.com",
    phone: "+91 98765 55555",
    location: "Mumbai, Maharashtra",

    headline: "Java Developer",
    experience: "2 Years",
    status: "ACTIVE",
    joinedDate: "12 Jul 2026",

    summary:
      "Java developer with experience in backend application development.",

    skills: ["Java", "Spring Boot", "SQL", "REST API"],

    education: [
      {
        id: 1,
        degree: "Bachelor of Engineering",
        specialization: "Computer Engineering",
        institute: "Mumbai Engineering College",
        year: "2018 - 2022",
        score: "7.6 CGPA",
      },
    ],

    applications: [
      {
        id: 606,
        position: "Java Developer",
        appliedDate: "21 Jul 2026",
        status: "REJECTED",
      },
    ],
  },
];

export const hrOffers = [
  {
    id: 901,
    candidateId: 510,
    applicationId: 610,

    candidate: "Abhijeet",
    initials: "AA",
    email: "abhijeetarekar@gmail.com",

    designation: "Java Developer",
    department: "Software Development",

    annualCTC: 650000,
    salaryDisplay: "₹6.5 LPA",

    joiningDate: "10 Aug 2026",
    issuedDate: "24 Jul 2026",
    expiryDate: "31 Jul 2026",

    location: "Pune, Maharashtra",
    employmentType: "Full Time",

    status: "SENT",
  },

  {
    id: 902,
    candidateId: 512,
    applicationId: 612,

    candidate: "Pranav",
    initials: "PF",
    email: "pranavfale@gmail.com",

    designation: "Frontend Developer",
    department: "Software Development",

    annualCTC: 720000,
    salaryDisplay: "₹7.2 LPA",

    joiningDate: "12 Aug 2026",
    issuedDate: "23 Jul 2026",
    expiryDate: "30 Jul 2026",

    location: "Mumbai, Maharashtra",
    employmentType: "Full Time",

    status: "ACCEPTED",
  },

  {
    id: 903,
    candidateId: 513,
    applicationId: 613,

    candidate: "Akash",
    initials: "AB",
    email: "akashbattula@gmail.com",

    designation: "Backend Developer",
    department: "Engineering",

    annualCTC: 600000,
    salaryDisplay: "₹6.0 LPA",

    joiningDate: "15 Aug 2026",
    issuedDate: "22 Jul 2026",
    expiryDate: "29 Jul 2026",

    location: "Pune, Maharashtra",
    employmentType: "Full Time",

    status: "DECLINED",
  },

  {
    id: 904,
    candidateId: 514,
    applicationId: 614,

    candidate: "Siddharth",
    initials: "SR",
    email: "siddharth@gmail.com",

    designation: "Software Engineer Trainee",
    department: "Engineering",

    annualCTC: 450000,
    salaryDisplay: "₹4.5 LPA",

    joiningDate: "18 Aug 2026",
    issuedDate: "24 Jul 2026",
    expiryDate: "02 Aug 2026",

    location: "Pune, Maharashtra",
    employmentType: "Full Time",

    status: "DRAFT",
  },
];
