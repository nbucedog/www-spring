package com.nbucedog.www.controller;

import com.nbucedog.www.dao.entity.Review;
import com.nbucedog.www.service.ReviewService;
import com.nbucedog.www.util.ResultTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller(value = "CommentController")
public class CommentController {
    @Autowired
    ReviewService reviewService;

    @RequestMapping(value = "/comment",method = RequestMethod.GET)
    @ResponseBody
    public Map getComment(){
        try {
            List<Review> reviewList = reviewService.findComments();
            return ResultTools.dataResult(0,reviewList);
        }catch (Exception e){
            return ResultTools.dataResult(1000,e.getMessage());
        }
    }
}
