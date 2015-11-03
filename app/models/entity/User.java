package models.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Required;
import play.mvc.Http.Context;
import utils.ModelUtil;

/**
 * User entity managed by Ebean
 */
@Entity
public class User extends AbstractTrailModel {

    /**
	 * serial version ID
	 */
	private static final long serialVersionUID = 1L;

	  @Id
    public Long id;
		@Required
		public String name;
		@Required
		public String password;
		@Required
		public Integer is_active;
		@OneToMany(cascade = CascadeType.ALL, mappedBy = "createUser")
		public List<Evaluation> evaluations = new ArrayList<Evaluation>();
		@OneToMany(cascade = CascadeType.ALL, mappedBy ="createUser")
		public List<Qanda> qandas = new ArrayList<Qanda>();
		@ManyToMany
    public List<Roll> rolls = new ArrayList<Roll>();

    /**
     * Generic query helper for entity User with id Long
     */
    public static Finder<Long,User> find = ModelUtil.getFinder(User.class);

		public static String getUserName(Long id){
			return find.byId(id).name;
		}

		public Integer getQuestionSize(){
			int count = 0;
			for(Qanda q : qandas){
				if(q.isQuestion == 1){
					count++;
				}
			}
			return count;
		}

		public Integer getAnswerSize(){
			int count = 0;
			for(Qanda q : qandas){
				if(q.isQuestion == 0){
					count++;
				}
			}
			return count;
		}

		/**
		 * 現在ログインしているユーザーのentityをセッションから取得する
		 * @return ログインユーザーのentity
		 */
		public static User getLoginUser(){
			return find.where().eq("name", Context.current().session().get("loginUser")).findUnique();
		}

		public static User createTestUser(){
			return find.byId(1L);
		}
}
