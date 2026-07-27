export const candidateStats = [
  {
    id: 1,
    title: "Available Jobs",
    value: 18,
    type: "jobs",
  },
  {
    id: 2,
    title: "My Applications",
    value: 5,
    type: "applications",
  },
  {
    id: 3,
    title: "Upcoming Interviews",
    value: 2,
    type: "interviews",
  },
  {
    id: 4,
    title: "Active Offers",
    value: 1,
    type: "offers",
  },
];

export const recentApplications = [
  {
    id: 201,
    jobId: 101,
    position: "Java Developer",
    department: "Software Development",
    location: "Pune, Maharashtra",
    appliedDate: "20 Jul 2026",
    lastUpdated: "23 Jul 2026",
    status: "SHORTLISTED",
    workMode: "HYBRID",
    employmentType: "FULL TIME",
    currentStage: 3,
    timeline: [
      {
        title: "Application Submitted",
        description: "Your application was successfully submitted.",
        date: "20 Jul 2026",
        completed: true,
      },
      {
        title: "Under Review",
        description: "HR reviewed your profile and resume.",
        date: "21 Jul 2026",
        completed: true,
      },
      {
        title: "Shortlisted",
        description: "You have been shortlisted for the interview process.",
        date: "23 Jul 2026",
        completed: true,
      },
      {
        title: "Interview",
        description: "Technical interview will be scheduled.",
        date: "Pending",
        completed: false,
      },
      {
        title: "Final Decision",
        description: "Final recruitment decision.",
        date: "Pending",
        completed: false,
      },
    ],
  },

  {
    id: 202,
    jobId: 102,
    position: "Frontend React Developer",
    department: "Software Development",
    location: "Mumbai, Maharashtra",
    appliedDate: "18 Jul 2026",
    lastUpdated: "21 Jul 2026",
    status: "UNDER_REVIEW",
    workMode: "HYBRID",
    employmentType: "FULL TIME",
    currentStage: 2,
    timeline: [
      {
        title: "Application Submitted",
        description: "Your application was successfully submitted.",
        date: "18 Jul 2026",
        completed: true,
      },
      {
        title: "Under Review",
        description: "Your profile is currently being reviewed by HR.",
        date: "21 Jul 2026",
        completed: true,
      },
      {
        title: "Shortlisting",
        description: "Waiting for the shortlisting decision.",
        date: "Pending",
        completed: false,
      },
      {
        title: "Interview",
        description: "Interview will be scheduled if shortlisted.",
        date: "Pending",
        completed: false,
      },
      {
        title: "Final Decision",
        description: "Final recruitment decision.",
        date: "Pending",
        completed: false,
      },
    ],
  },

  {
    id: 203,
    jobId: 104,
    position: "Backend Developer",
    department: "Software Development",
    location: "Bengaluru, Karnataka",
    appliedDate: "15 Jul 2026",
    lastUpdated: "15 Jul 2026",
    status: "APPLIED",
    workMode: "ONSITE",
    employmentType: "FULL TIME",
    currentStage: 1,
    timeline: [
      {
        title: "Application Submitted",
        description: "Your application was successfully submitted.",
        date: "15 Jul 2026",
        completed: true,
      },
      {
        title: "Under Review",
        description: "Waiting for HR to review your application.",
        date: "Pending",
        completed: false,
      },
      {
        title: "Shortlisting",
        description: "Waiting for shortlisting.",
        date: "Pending",
        completed: false,
      },
      {
        title: "Interview",
        description: "Interview will be scheduled if shortlisted.",
        date: "Pending",
        completed: false,
      },
      {
        title: "Final Decision",
        description: "Final recruitment decision.",
        date: "Pending",
        completed: false,
      },
    ],
  },

  {
    id: 204,
    jobId: 103,
    position: "Software Engineer Trainee",
    department: "Engineering",
    location: "Pune, Maharashtra",
    appliedDate: "12 Jul 2026",
    lastUpdated: "18 Jul 2026",
    status: "REJECTED",
    workMode: "ONSITE",
    employmentType: "FULL TIME",
    currentStage: 2,
    timeline: [
      {
        title: "Application Submitted",
        description: "Your application was successfully submitted.",
        date: "12 Jul 2026",
        completed: true,
      },
      {
        title: "Application Reviewed",
        description: "HR completed the initial profile review.",
        date: "16 Jul 2026",
        completed: true,
      },
      {
        title: "Not Selected",
        description: "Your application was not selected for the next stage.",
        date: "18 Jul 2026",
        completed: false,
        rejected: true,
      },
    ],
  },

  {
    id: 205,
    jobId: 105,
    position: "Associate Software Developer",
    department: "Engineering",
    location: "Hyderabad, Telangana",
    appliedDate: "10 Jul 2026",
    lastUpdated: "22 Jul 2026",
    status: "INTERVIEW_SCHEDULED",
    workMode: "REMOTE",
    employmentType: "FULL TIME",
    currentStage: 4,
    timeline: [
      {
        title: "Application Submitted",
        description: "Your application was successfully submitted.",
        date: "10 Jul 2026",
        completed: true,
      },
      {
        title: "Under Review",
        description: "HR reviewed your application.",
        date: "13 Jul 2026",
        completed: true,
      },
      {
        title: "Shortlisted",
        description: "Your profile was shortlisted.",
        date: "18 Jul 2026",
        completed: true,
      },
      {
        title: "Interview Scheduled",
        description: "Your technical interview has been scheduled.",
        date: "26 Jul 2026",
        completed: true,
      },
      {
        title: "Final Decision",
        description: "Waiting for interview completion.",
        date: "Pending",
        completed: false,
      },
    ],
  },
];

export const upcomingInterviews = [
  {
    id: 301,
    applicationId: 201,
    jobId: 101,

    position: "Java Developer",
    company: "RecruitFlow Technologies",
    department: "Software Development",

    round: "TECHNICAL",
    roundNumber: 1,

    date: "26 Jul 2026",
    time: "11:00 AM",
    duration: "60 Minutes",

    mode: "ONLINE",
    status: "SCHEDULED",

    interviewer: "Rahul Mehta",

    meetingLink: "https://meet.google.com/",

    instructions:
      "Please join the meeting 10 minutes before the scheduled time. Keep your resume and a valid ID ready.",

    topics: ["Core Java", "OOP", "Collections", "Spring Boot Basics", "SQL"],
  },

  {
    id: 302,
    applicationId: 202,
    jobId: 102,

    position: "Frontend React Developer",
    company: "RecruitFlow Technologies",
    department: "Software Development",

    round: "HR",
    roundNumber: 2,

    date: "28 Jul 2026",
    time: "03:30 PM",
    duration: "30 Minutes",

    mode: "ONLINE",
    status: "SCHEDULED",

    interviewer: "Priya Kulkarni",

    meetingLink: "https://meet.google.com/",

    instructions:
      "Please join the meeting 5 minutes before the scheduled interview.",

    topics: [
      "Introduction",
      "Project Discussion",
      "Communication",
      "Career Goals",
    ],
  },

  {
    id: 303,
    applicationId: 205,
    jobId: 105,

    position: "Associate Software Developer",
    company: "RecruitFlow Technologies",
    department: "Engineering",

    round: "TECHNICAL",
    roundNumber: 1,

    date: "22 Jul 2026",
    time: "10:30 AM",
    duration: "60 Minutes",

    mode: "ONLINE",
    status: "COMPLETED",

    interviewer: "Amit Sharma",

    meetingLink: "",

    instructions: "",

    topics: ["Java", "Data Structures", "SQL", "Problem Solving"],

    result: "PASSED",

    feedback:
      "Candidate demonstrated good programming fundamentals and problem-solving ability.",
  },

  {
    id: 304,
    applicationId: 203,
    jobId: 104,

    position: "Backend Developer",
    company: "RecruitFlow Technologies",
    department: "Software Development",

    round: "SCREENING",
    roundNumber: 1,

    date: "18 Jul 2026",
    time: "02:00 PM",
    duration: "30 Minutes",

    mode: "ONLINE",
    status: "COMPLETED",

    interviewer: "Neha Patil",

    meetingLink: "",

    instructions: "",

    topics: ["Java Basics", "REST API", "SQL"],

    result: "PASSED",

    feedback: "Candidate cleared the initial screening round.",
  },

  {
    id: 305,
    applicationId: 204,
    jobId: 103,

    position: "Software Engineer Trainee",
    company: "RecruitFlow Technologies",
    department: "Engineering",

    round: "TECHNICAL",
    roundNumber: 1,

    date: "17 Jul 2026",
    time: "04:00 PM",
    duration: "45 Minutes",

    mode: "ONLINE",
    status: "CANCELLED",

    interviewer: "Vikram Joshi",

    meetingLink: "",

    instructions: "",

    topics: ["Programming Fundamentals", "OOP", "SQL"],
  },
];

export const latestJobs = [
  {
    id: 101,
    title: "Java Developer",
    department: "Software Development",
    location: "Pune, Maharashtra",
    experience: "0 - 2 Years",
    workMode: "HYBRID",
    employmentType: "FULL TIME",
    skills: ["Java", "Spring Boot", "MySQL"],
    deadline: "10 Aug 2026",
    posted: "2 days ago",
    openings: 4,
    salary: "₹4.5 - 7 LPA",
    description:
      "We are looking for a Java Developer to build scalable web applications and REST APIs using Java and Spring Boot.",
    responsibilities: [
      "Develop and maintain Java-based applications.",
      "Build RESTful APIs using Spring Boot.",
      "Work with MySQL databases.",
      "Write clean, reusable and maintainable code.",
      "Collaborate with frontend and QA teams.",
    ],
    requirements: [
      "Strong understanding of Core Java and OOP.",
      "Basic knowledge of Spring Boot.",
      "Understanding of SQL and relational databases.",
      "Knowledge of REST APIs.",
      "Good problem-solving skills.",
    ],
  },

  {
    id: 102,
    title: "Frontend React Developer",
    department: "Software Development",
    location: "Mumbai, Maharashtra",
    experience: "1 - 3 Years",
    workMode: "HYBRID",
    employmentType: "FULL TIME",
    skills: ["React", "JavaScript", "HTML", "CSS"],
    deadline: "12 Aug 2026",
    posted: "3 days ago",
    openings: 3,
    salary: "₹5 - 8 LPA",
    description:
      "Join our frontend team to build responsive and reusable user interfaces using React and modern JavaScript.",
    responsibilities: [
      "Develop reusable React components.",
      "Build responsive web interfaces.",
      "Integrate frontend applications with REST APIs.",
      "Improve application usability and performance.",
      "Collaborate with backend developers.",
    ],
    requirements: [
      "Knowledge of React and JavaScript.",
      "Strong HTML and CSS fundamentals.",
      "Understanding of component-based architecture.",
      "Basic knowledge of REST APIs.",
      "Familiarity with Git.",
    ],
  },

  {
    id: 103,
    title: "Software Engineer Trainee",
    department: "Engineering",
    location: "Bengaluru, Karnataka",
    experience: "0 - 1 Years",
    workMode: "ONSITE",
    employmentType: "FULL TIME",
    skills: ["Java", "DSA", "SQL"],
    deadline: "15 Aug 2026",
    posted: "4 days ago",
    openings: 8,
    salary: "₹3.5 - 5 LPA",
    description:
      "An entry-level opportunity for graduates interested in software engineering and enterprise application development.",
    responsibilities: [
      "Participate in software development activities.",
      "Solve programming and data structure problems.",
      "Assist senior developers with assigned modules.",
      "Write and test application code.",
      "Participate in technical training.",
    ],
    requirements: [
      "Graduate in Engineering or related discipline.",
      "Basic programming knowledge.",
      "Understanding of data structures.",
      "Basic SQL knowledge.",
      "Willingness to learn new technologies.",
    ],
  },

  {
    id: 104,
    title: "Backend Developer",
    department: "Software Development",
    location: "Pune, Maharashtra",
    experience: "1 - 3 Years",
    workMode: "ONSITE",
    employmentType: "FULL TIME",
    skills: ["Java", "Spring Boot", "REST API", "MySQL"],
    deadline: "18 Aug 2026",
    posted: "5 days ago",
    openings: 2,
    salary: "₹5 - 9 LPA",
    description:
      "We are hiring a Backend Developer to develop secure APIs and scalable business services.",
    responsibilities: [
      "Develop backend services.",
      "Design REST APIs.",
      "Integrate relational databases.",
      "Implement business logic.",
      "Participate in code reviews.",
    ],
    requirements: [
      "Java programming knowledge.",
      "Spring Boot fundamentals.",
      "REST API knowledge.",
      "SQL knowledge.",
      "Understanding of Git.",
    ],
  },

  {
    id: 105,
    title: "Associate Software Developer",
    department: "Engineering",
    location: "Hyderabad, Telangana",
    experience: "0 - 2 Years",
    workMode: "REMOTE",
    employmentType: "FULL TIME",
    skills: ["Java", "JavaScript", "SQL"],
    deadline: "20 Aug 2026",
    posted: "6 days ago",
    openings: 5,
    salary: "₹4 - 6 LPA",
    description:
      "Work with our engineering team on full-stack enterprise applications and internal platforms.",
    responsibilities: [
      "Implement assigned software features.",
      "Fix application defects.",
      "Write maintainable code.",
      "Work with REST APIs and databases.",
      "Participate in team discussions.",
    ],
    requirements: [
      "Programming fundamentals.",
      "OOP knowledge.",
      "Basic JavaScript knowledge.",
      "SQL fundamentals.",
      "Good communication skills.",
    ],
  },

  {
    id: 106,
    title: "QA Engineer",
    department: "Quality Assurance",
    location: "Pune, Maharashtra",
    experience: "0 - 2 Years",
    workMode: "HYBRID",
    employmentType: "FULL TIME",
    skills: ["Testing", "SQL", "Postman", "API Testing"],
    deadline: "22 Aug 2026",
    posted: "1 week ago",
    openings: 3,
    salary: "₹3.5 - 5.5 LPA",
    description:
      "We are looking for a QA Engineer to test web applications, APIs and recruitment workflows.",
    responsibilities: [
      "Create and execute test cases.",
      "Perform functional testing.",
      "Test REST APIs.",
      "Report and track defects.",
      "Work closely with developers.",
    ],
    requirements: [
      "Software testing fundamentals.",
      "Basic SQL knowledge.",
      "Knowledge of API testing.",
      "Attention to detail.",
      "Good analytical skills.",
    ],
  },
];

export const candidateOffers = [
  {
    id: 401,
    applicationId: 201,
    jobId: 101,

    position: "Java Developer",
    department: "Software Development",
    company: "RecruitFlow Technologies",
    location: "Pune, Maharashtra",

    status: "PENDING",

    offeredDate: "23 Jul 2026",
    expiryDate: "30 Jul 2026",
    joiningDate: "10 Aug 2026",

    employmentType: "FULL TIME",
    workMode: "HYBRID",

    annualCTC: "₹6,50,000",
    monthlyGross: "₹54,167",

    probationPeriod: "6 Months",
    noticePeriod: "60 Days",

    benefits: [
      "Health Insurance",
      "Provident Fund",
      "Performance Bonus",
      "Paid Leave",
      "Learning & Development",
    ],

    message:
      "Congratulations! We are pleased to offer you the position of Java Developer at RecruitFlow Technologies.",
  },

  {
    id: 402,
    applicationId: 205,
    jobId: 105,

    position: "Associate Software Developer",
    department: "Engineering",
    company: "RecruitFlow Technologies",
    location: "Hyderabad, Telangana",

    status: "ACCEPTED",

    offeredDate: "15 Jul 2026",
    expiryDate: "20 Jul 2026",
    joiningDate: "05 Aug 2026",

    employmentType: "FULL TIME",
    workMode: "REMOTE",

    annualCTC: "₹5,20,000",
    monthlyGross: "₹43,333",

    probationPeriod: "6 Months",
    noticePeriod: "60 Days",

    benefits: [
      "Health Insurance",
      "Provident Fund",
      "Paid Leave",
      "Remote Work",
    ],

    message:
      "Congratulations! Your selection process has been completed successfully.",
  },

  {
    id: 403,
    applicationId: 202,
    jobId: 102,

    position: "Frontend React Developer",
    department: "Software Development",
    company: "RecruitFlow Technologies",
    location: "Mumbai, Maharashtra",

    status: "REJECTED",

    offeredDate: "10 Jul 2026",
    expiryDate: "16 Jul 2026",
    joiningDate: "01 Aug 2026",

    employmentType: "FULL TIME",
    workMode: "HYBRID",

    annualCTC: "₹7,00,000",
    monthlyGross: "₹58,333",

    probationPeriod: "6 Months",
    noticePeriod: "60 Days",

    benefits: [
      "Health Insurance",
      "Provident Fund",
      "Performance Bonus",
      "Paid Leave",
    ],

    message:
      "We are pleased to offer you the position of Frontend React Developer.",
  },
];

export const candidateProfile = {
  id: 501,

  firstName: "Abhijeet",
  lastName: "Arekar",

  email: "abhijeetarekar@gmail.com",
  phone: "+91 98765 43210",

  location: "Pune, Maharashtra",

  headline: "Java Full Stack Developer",

  summary:
    "Motivated software developer with strong fundamentals in Java, Spring Boot, React, SQL and Data Structures. Interested in building scalable web applications and solving real-world business problems.",

  profileCompletion: 85,

  skills: [
    "Java",
    "Spring Boot",
    "React",
    "JavaScript",
    "HTML",
    "CSS",
    "MySQL",
    "REST API",
    "Git",
  ],

  education: [
    {
      id: 1,
      degree: "Bachelor of Technology",
      specialization: "Computer Science & Engineering",
      institute: "Jspm Institute of Technology",
      location: "Pune, Maharashtra",
      startYear: "2019",
      endYear: "2023",
      score: "8.2 CGPA",
    },

    {
      id: 2,
      degree: "Post Graduate Diploma",
      specialization: "Advanced Computing",
      institute: "C-DAC Training Centre",
      location: "Maharashtra",
      startYear: "2026",
      endYear: "2026",
      score: "Pursuing",
    },
  ],

  experience: [
    {
      id: 1,
      role: "Software Developer Intern",
      company: "TechNova Solutions",
      location: "Pune, Maharashtra",
      startDate: "Jan 2023",
      endDate: "Jun 2023",
      description:
        "Worked on Java-based web applications, REST APIs and database integration as part of the development team.",
    },
  ],

  resume: {
    fileName: "Abhijeet_Arekar_Resume.pdf",
    uploadedDate: "20 Jul 2026",
  },
};
