package com.nbucedog.www.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbucedog.www.dao.entity.Article;
import com.nbucedog.www.dao.entity.Review;
import com.nbucedog.www.dao.entity.ReviewReply;
import com.nbucedog.www.dao.entity.Tag;
import com.nbucedog.www.model.Articles;
import com.nbucedog.www.service.*;
import com.nbucedog.www.util.CookieTools;
import com.nbucedog.www.util.ResultTools;
import com.nbucedog.www.util.SplitTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.*;

@Controller(value = "BlogController")
public class BlogController {
    @Autowired
    ArticleService articleService;
    @Autowired
    TagService tagService;
    @Autowired
    UserService userService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewReplyService reviewReplyService;
    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping(value = "/articles",method = RequestMethod.GET)
    @ResponseBody
    public Map getArticles(HttpServletRequest request,Integer pageIndex,Integer pageSize){
        if(pageIndex==null || pageSize==null){
            List<Articles> articlesList = articleService.getArticles(0,5,null).getContent();
            return ResultTools.dataResult(0,articlesList);
        }
        else if(request.getParameter("tagId")!=null){
            int tagId = Integer.valueOf(request.getParameter("tagId"));
            List<Article> articleList = tagService.findById(tagId).getArticleList();
            long total = articleList.size();
            long pageStart = pageIndex*pageSize;
            long pageEnd = pageStart+pageSize;
            if(total<pageEnd){
                pageEnd = total;
            }
            if(pageStart<=pageEnd){
                List<Articles> articlesList = new ArrayList<>();
                for (long i=pageStart;i<pageEnd;i++){
                    articlesList.add(new Articles(articleList.get((int)i)));
                }
                return ResultTools.dataResult(0,total,articlesList);
            }else{
                return ResultTools.dataResult(700,total,null);
            }
        }
        else {
            Article article = new Article();
            if (request.getParameter("userId")!=null){
                int userId = Integer.valueOf(request.getParameter("userId"));
                article.setUser(userService.findById(userId));
            }
            Page<Articles> articlesPage = articleService.getArticles(pageIndex,pageSize,article.getUser());
            long total = articlesPage.getTotalElements();
            List<Articles> articlesList = articlesPage.getContent();
            return ResultTools.dataResult(0,total,articlesList);
        }
//        else {
//            Article article = new Article();
//            article.setPublish(true);
//            if (request.getParameter("userId")!=null){
//                int userId = Integer.valueOf(request.getParameter("userId"));
//                article.setUser(userService.findById(userId));
//            }
//            ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id","views","thumbs");
//            Example<Article> articleExample = Example.of(article,matcher);
//            Page<Article> articlePage = articleService.findByPage(pageIndex,pageSize,articleExample);
//            List<Article> articleList = articlePage.getContent();
//            long total = articlePage.getTotalElements();
//            return ResultTools.dataResult(0,total,articleList);
//        }
    }

    @RequestMapping(value = "/article",method = RequestMethod.GET)
    @ResponseBody
    public void getArticle(Integer id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        Map map;
        if(id==null) {
            map = ResultTools.result(700);
            response.getWriter().write(objectMapper.writeValueAsString(map));
            return;
        }
        Article article = articleService.findById(id);
        if(article.getPublish()){
            Cookie[] cookies = request.getCookies();
            String viewed=null;
            String username=null;
            if(cookies!=null){
                for (Cookie cookie:cookies){
                    switch (cookie.getName()){
                        case "viewed":
                            viewed=cookie.getValue();
                        case "username":
                            username=cookie.getValue();
                    }
                }
            }
            String newViewed=SplitTools.insertNum2Str(viewed,article.getId());
            if(!newViewed.equals(viewed) && !article.getUser().getUsername().equals(username)){
                articleService.updateViewsById(article.getId(),article.getViews()+1);
            }
            Cookie cookie = CookieTools.buildCookie("viewed",newViewed,2592000,true);
            response.addCookie(cookie);
            map = ResultTools.dataResult(0,article);
            response.setStatus(HttpStatus.OK.value());
        }else {
            map = ResultTools.result(404);
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
        response.getWriter().write(objectMapper.writeValueAsString(map));
    }

    private Set<Tag> saveOrUpdateTagSet(Set<Tag> tagSet){
        String[] colors = new String[]{"primary","secondary","success","danger","warning","info"};
        Set<Tag> newTagSet = new HashSet<>();
        for(Tag tag:tagSet){
            Tag oldTag = tagService.findByTag(tag.getTag());
            if(oldTag==null){
                Tag newTag = new Tag();
                newTag.setTag(tag.getTag());
                int i = new Random().nextInt(colors.length);
                newTag.setColor(colors[i]);
                tagService.save(newTag);
                newTagSet.add(newTag);
            }else {
                newTagSet.add(oldTag);
            }
        }
        return newTagSet;
    }


    @RequestMapping(value = "/article",method = RequestMethod.POST)
    @ResponseBody
    public Map articleUpload(@RequestBody Article article){
        article.setDate(new Date());
        article.setTagSet(saveOrUpdateTagSet(article.getTagSet()));
        articleService.save(article);
        return ResultTools.result(0);
    }

    @RequestMapping(value = "/article",method = RequestMethod.PUT)
    @ResponseBody
    public Map articleUpdate(@RequestBody Article article){
        Article oldArticle = articleService.findById(article.getId());
        oldArticle.setTagSet(saveOrUpdateTagSet(article.getTagSet()));
        oldArticle.setTitle(article.getTitle());
        oldArticle.setSummary(article.getSummary());
        oldArticle.setContent(article.getContent());
        oldArticle.setPublish(article.getPublish());
        articleService.update(oldArticle);
        return ResultTools.result(0);
    }

    @RequestMapping(value = "/article",method = RequestMethod.DELETE)
    @ResponseBody
    public Map articleDelete(Integer id){
        Article article = articleService.findById(id);
        articleService.delete(article);
        return ResultTools.result(0);
    }

    @RequestMapping(value = "/review",method = RequestMethod.GET)
    @ResponseBody
    public Map getReview(Integer articleId){
        List<Review> reviewList = reviewService.findAllByArticleId(articleId);
        return ResultTools.dataResult(0,reviewList);
    }

    @RequestMapping(value = "/review",method = RequestMethod.POST)
    @ResponseBody
    public Map postReview(@RequestBody Review review){
        review.setDate(new Date());
        reviewService.save(review);
        return ResultTools.result(0);
    }

    @RequestMapping(value = "/review",method = RequestMethod.DELETE)
    @ResponseBody
    public Map deleteReview(Integer id){
        Review review = reviewService.findById(id);
        reviewService.delete(review);
        return ResultTools.result(0);
    }

    @RequestMapping(value = "/reviewReply",method = RequestMethod.POST)
    @ResponseBody
    public Map postReplyReview(@RequestBody ReviewReply reviewReply){
//        Timestamp timestamp = new Timestamp(new Date().getTime());
        reviewReply.setDate(new Date());
        reviewReplyService.save(reviewReply);
        return ResultTools.result(0);
    }

    @RequestMapping(value = "/reviewReply",method = RequestMethod.DELETE)
    @ResponseBody
    public Map deleteReviewReply(Integer id){
        ReviewReply reviewReply = reviewReplyService.findById(id);
        reviewReplyService.delete(reviewReply);
        return ResultTools.result(0);
    }

    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    @ResponseBody
    public Map postTag(@RequestBody Tag tag){
        tagService.save(tag);
        return ResultTools.result(0);
    }
}
