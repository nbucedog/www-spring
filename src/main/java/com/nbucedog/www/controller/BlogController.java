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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        try {
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
//            else {
//                Article article = new Article();
//                article.setPublish(true);
//                if (request.getParameter("userId")!=null){
//                    int userId = Integer.valueOf(request.getParameter("userId"));
//                    article.setUser(userService.findById(userId));
//                }
//                ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id","views","thumbs");
//                Example<Article> articleExample = Example.of(article,matcher);
//                Page<Article> articlePage = articleService.findByPage(pageIndex,pageSize,articleExample);
//                List<Article> articleList = articlePage.getContent();
//                long total = articlePage.getTotalElements();
//                return ResultTools.dataResult(0,total,articleList);
//            }

        }catch (Exception e){
            e.printStackTrace();
            return ResultTools.dataResult(1000,e.getMessage());
        }
    }

    @RequestMapping(value = "/article",method = RequestMethod.GET)
    @ResponseBody
    public void getArticle(Integer id, HttpServletRequest request, HttpServletResponse response){
        try {
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
        }catch (Exception e){
            e.printStackTrace();
        }
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

    @PreAuthorize("#article.user.id==authentication.principal.id")
    @RequestMapping(value = "/article",method = RequestMethod.POST)
    @ResponseBody
    public Map articleUpload(@RequestBody Article article){
        try {
            if(article.getId()==null){
                article.setDate(new Date());
                article.setTagSet(saveOrUpdateTagSet(article.getTagSet()));
                articleService.save(article);
                return ResultTools.result(0);
            }else {
                return ResultTools.result(403);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultTools.dataResult(1000,e.getMessage());
        }
    }

    @PreAuthorize("#article.user.id==authentication.principal.id")
    @RequestMapping(value = "/article",method = RequestMethod.PUT)
    @ResponseBody
    public Map articleUpdate(@RequestBody Article article){
        try {
            Article oldArticle = articleService.findById(article.getId());
            if(!(oldArticle.getUser().getId()==article.getUser().getId())){
                return ResultTools.result(403);
            }
            oldArticle.setTagSet(saveOrUpdateTagSet(article.getTagSet()));
            oldArticle.setTitle(article.getTitle());
            oldArticle.setSummary(article.getSummary());
            oldArticle.setContent(article.getContent());
            oldArticle.setPublish(article.getPublish());
            articleService.update(oldArticle);
            return ResultTools.result(0);
        }catch (Exception e){
            e.printStackTrace();
            return ResultTools.dataResult(1000,e.getMessage());
        }
    }
    @RequestMapping(value = "/article",method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_MASTER")
    @PreAuthorize("#article.user.id==authentication.principal.id or hasAuthority('delete_article')")
    public Map articleDelete(@RequestBody Article article){
        try {
            Article oldArticle = articleService.findById(article.getId());
            if(!(oldArticle.getUser().getId()==article.getUser().getId())){
                return ResultTools.result(403);
            }
            articleService.delete(article);
            return ResultTools.result(0);
        }catch (Exception e){
            return ResultTools.dataResult(1000,e.getMessage());
        }

    }

    @RequestMapping(value = "/review",method = RequestMethod.GET)
    @ResponseBody
    public Map getReview(Integer articleId){
        try {
            List<Review> reviewList = reviewService.findAllByArticleId(articleId);
            return ResultTools.dataResult(0,reviewList);
        }catch (Exception e){
            return ResultTools.dataResult(1000,e.getMessage());
        }
    }

    @RequestMapping(value = "/review",method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("#review.user.id==authentication.principal.id")
    public Map postReview(@RequestBody Review review){
        try {
            if(review.getId()!=null){
                return ResultTools.result(403);
            }
            review.setDate(new Date());
            reviewService.save(review);
            return ResultTools.result(0);
        }catch (Exception e){
            return ResultTools.dataResult(1000,e.getMessage());
        }
    }

    @RequestMapping(value = "/reviewReply",method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("#reviewReply.user.id==authentication.principal.id")
    public Map postReplyReview(@RequestBody ReviewReply reviewReply){
        try {
            if(reviewReply.getId()==null){
//                Timestamp timestamp = new Timestamp(new Date().getTime());
                reviewReply.setDate(new Date());
                reviewReplyService.save(reviewReply);
                return ResultTools.result(0);
            }
            else {
                return ResultTools.result(403);
            }
        }catch (Exception e){
            return ResultTools.dataResult(1000,e.getMessage());
        }
    }

    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('add_tag')")
    public Map postTag(@RequestBody Tag tag){
        try {
            tagService.save(tag);
            return ResultTools.result(0);
        }catch (Exception e){
            e.printStackTrace();
            return ResultTools.dataResult(1000,e.getMessage());
        }
    }
}
