package com.nbucedog.www.dao.repository;

import com.nbucedog.www.dao.entity.ReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("ReviewReplyDAO")
public interface ReviewReplyDAO extends JpaRepository<ReviewReply,Integer>, JpaSpecificationExecutor<ReviewReply> {

}
