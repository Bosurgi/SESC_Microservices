package com.sesc.studentportal.misc;

//@Configuration
//public class PopulateModules {
//
//    @Bean
//    CommandLineRunner initDatabase(ModuleRepository moduleRepository) {
//        List<Module> moduleList = new ArrayList<>();
//
//        moduleList.add(new Module("Computer Network Architecture",
//                "This module will teach the basics of networking architecture and how to configure a small network.", 350.00));
//
//        moduleList.add(new Module("Database Management Systems",
//                "This module covers the fundamental concepts of database management systems and SQL.", 600.00));
//
//        moduleList.add(new Module("Software Engineering",
//                "This module focuses on software development methodologies, project management, and quality assurance.",
//                800.00));
//
//        moduleList.add(new Module("Data Structures and Algorithms",
//                "This module explores data structures such as arrays, linked lists, trees, and algorithms for sorting and searching.",
//                450.00));
//
//        moduleList.add(new Module("Artificial Intelligence",
//                "This module introduces techniques used in artificial intelligence, including machine learning, neural networks, and natural language processing.",
//                900.00));
//
//        moduleList.add(new Module("Cybersecurity",
//                "This module covers cybersecurity principles, threats, cryptography, and best practices for securing systems and networks.",
//                700.00));
//
//        return args -> {
//            moduleRepository.saveAll(moduleList);
//        };
//    }
//}
