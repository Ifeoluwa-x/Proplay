//package com.soccerapp.app.scheduler;
//
//import com.soccerapp.app.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MyScheduler {
//
//    @Autowired
//    private UserService userService;
//
//    private boolean toggle = true;
//
//    @Scheduled(fixedRate = 20000)  // Every 20 seconds
//    public void toggleUserFname() {
//        if (toggle) {
//            // Change all 'Ebun' to 'Abigail'
//            userService.toggleFname("Ebun", "Abigail");
//            System.out.println("Changed 'Ebun' to 'Abigail'");
//        } else {
//            // Change all 'Abigail' back to 'Ebun'
//            userService.toggleFname("Abigail", "Ebun");
//            System.out.println("Changed 'Abigail' back to 'Ebun'");
//        }
//        toggle = !toggle;  // Flip the toggle
//    }
//}
