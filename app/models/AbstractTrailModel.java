package models;

import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import com.avaje.ebean.*;
import utils.DateUtil;
import models.User;

/**
 * abstract model defining common column
 */
@Entity
public abstract class AbstractTrailModel extends Model {

    public static final int INSERT = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;

    @Constraints.Required
    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date create_time;
    @ManyToOne(cascade = CascadeType.ALL)
    @Constraints.Required
    public User createUser;
    @Constraints.Required
    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date update_time;
    @ManyToOne(cascade = CascadeType.ALL)
    @Constraints.Required
    public User updateUser;
    @Constraints.Required
		public Integer is_delete;

    /**
     * 保存処理
     */
    public void save(User loginUser){
      if(create_time != null){
        setTrailInfo(UPDATE, loginUser);
        super.save();

      }else{
        setTrailInfo(INSERT, loginUser);
        super.save();
      }
    }

  /**
   * 最終更新からの経過時間を取得する。
   * @return 最終更新からの経過時間
   */
  public String getSinceLastUpdate(){
    return DateUtil.howLongFromNow(update_time);
  }

  /**
   * <pre>
   * 追跡情報をセットする。
   * 設定されるカラム...create_time/create_user_id.update_time/update_user_id
   * </pre>
   */
  public void setTrailInfo(int code, User loginUser){

    switch(code) {
      case INSERT:
        create_time = DateUtil.now();
        createUser = loginUser;
      case UPDATE:
      case DELETE:
        update_time = DateUtil.now();
        updateUser = loginUser;
    }

    if(code == DELETE){
      is_delete = 1;

    }else{
      is_delete = 0;
    }
  }
}
