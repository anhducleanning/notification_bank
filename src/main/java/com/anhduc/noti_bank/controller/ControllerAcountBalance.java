package com.anhduc.noti_bank.controller;

import com.anhduc.noti_bank.model.AccountBalance;
import com.anhduc.noti_bank.model.AddBalance;
import com.anhduc.noti_bank.model.ResponseJson;
import com.anhduc.noti_bank.sendmesenger.GmailBot;
import com.anhduc.noti_bank.sendmesenger.TelegramBot;
import com.anhduc.noti_bank.serivce.ServiceAccountBalance;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ControllerAcountBalance {

    private static Logger log = Logger.getLogger(ControllerAcountBalance.class);

    @Value("${notification.channnel}")
    private String chanel;
    @Autowired
    ServiceAccountBalance blance;

    @Autowired
    TelegramBot telegramBot;

    @Autowired
    GmailBot gmailBot;

    @GetMapping("/test")
    public List<AccountBalance> test(){
        AccountBalance ac = new AccountBalance("1","2", 3000000L,"BC","2");
        List<AccountBalance> courses = new ArrayList<>();
        courses.add(ac);
        return courses ;
    }

    @GetMapping("/sendmail")
    public void sendMail()  {
        System.out.println("Đang  gửi mmail");
          try {
              gmailBot.sendMailTest("hoangnv06042002@gmail.com");
              System.out.println("Send Mail Susses");
              log.info("Send Mail Susses");
          }catch (Exception e){
              e.printStackTrace();
          }
    }

    @GetMapping("/notifi_balance")
    public ResponseEntity<?> getAllCourse(){
        ResponseJson json = new ResponseJson(0,"Thanh cong");
        List<AccountBalance> balances = blance.getNoti();

        //SEND MAIL

        if(chanel == "GMAIL"){
            sendMail();
        }

        if(balances != null){
            log.info("Get Data Sucsess");
            return new ResponseEntity<>(balances, HttpStatus.OK);

        }else{
            log.info("Data is null");
        }
            return new ResponseEntity<>(json,HttpStatus.OK);

    }

    @PostMapping("/add")
   public ResponseEntity<?> addCourse(@RequestBody AccountBalance balance) {
        if (balance.getTitle()=="") {
            System.out.println("Null Objejct");
            AddBalance json = new AddBalance(0, "Không thành công", LocalDate.now().toString());
            return new ResponseEntity<>(json, HttpStatus.CREATED);

        } else {
            AccountBalance newCourse = blance.create(balance);
            ResponseJson json = new ResponseJson(0, "Thanh cong");
            return new ResponseEntity<>(json, HttpStatus.CREATED);
        }

    }
}
