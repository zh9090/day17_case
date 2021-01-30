package cn.itcast.dao.impl;

import cn.itcast.dao.UserDao;
import cn.itcast.domain.User;
import cn.itcast.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static javax.swing.UIManager.get;

/**
 * 实现类
 */
public class UserDaoImpl  implements UserDao {
    //声明一个JdbcTemplate类，就可以操作数据库了
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public List<User> findAll() {
        //使用jdbc操作数据库
        //1.定义sql
        String sql ="select * from user";
        //使用Spring的JdbcTemplate查询数据库，获取List结果列表，数据库表字段和实体类自动对应，可以使用
        List<User> users = template.query(sql, new BeanPropertyRowMapper<User>(User.class));
        return users;
    }
    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        try {
            String sql = "select * from user where username = ? and password = ?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 方法的实现类声明出来
     *
     * @param user
     */
    @Override
    public void add(User user) {
        //定义SQL语句
        String sql = "insert into user values(null,?,?,?,?,?,?,null,null)";
        //2.执行sql
        template.update(sql,user.getName(),user.getGender(),user.getAge(),user.getAddress(),user.getQq(),user.getEmail());

    }

    /**
     * 删除
     * @param
     */
    @Override
    public void delete(int id) {
        //1定义sql
        String sql = "delete from user where id = ?";
        //执行sql
        template.update(sql,id);

    }

    /**
     * 查找id修改
     * @param
     * @return
     */
    @Override
    public User findById(int id) {
        String sql = "select * from user where id = ?";
        //调用方法template传入SQL语句
        return template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),id);
    }

    @Override
    public void update(User user) {
        String sql = "update user set name= ?, gender= ?, age= ?, address= ?, qq= ?,email= ? where id = ?";
        template.update(sql,user.getName(),user.getGender(),user.getAge(),user.getAddress(),user.getQq(),user.getEmail(),user.getId());
    }

    @Override
    public int findTotalCount(Map<String, String[]> condition) {
        //定义一个模板初始化sql
        String sql ="select count(*) from user where 1 = 1";
        StringBuilder sb = new StringBuilder(sql);
        //2遍历map
        Set<String> keySet = condition.keySet();
        //定义一个参数的集合
        List<Object> params = new ArrayList<Object>();
        for (String key: keySet){
            //select count(*) from user where 1 = 1 and currentPage like ?  and rows like ? 排除分页条件的参数
            if ("currentPage".equals(key) || "rows".equals(key)){
                continue;//结束当前的循环，继续下一次循环
            }

            //获取value
            String value = condition.get(key)[0];
            //判断value是否有值
            if (value != null && !"".equals(value)){
                //有值，拼接sql
                sb.append(" and "+key+" like ? ");
                params.add("%"+value+"%");//加条件的值
            }
        }
        System.out.println(sb.toString());
        System.out.println(params);
        return template.queryForObject(sb.toString(),Integer.class,params.toArray());//最后转换成一个数组
    }

    @Override
    public List<User> findByPage(int start, int rows, Map<String, String[]> condition) {

        String sql ="select * from user where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        //2遍历map
        Set<String> keySet = condition.keySet();
        //定义一个参数的集合
        List<Object> params = new ArrayList<Object>();
        for (String key: keySet){
            //select count(*) from user where 1 = 1 and currentPage like ?  and rows like ? 排除分页条件的参数
            if ("currentPage".equals(key) || "rows".equals(key)){
                continue;//结束当前的循环，继续下一次循环
            }

            //获取value
            String value = condition.get(key)[0];
            //判断value是否有值
            if (value != null && !"".equals(value)){
                //有值，拼接sql
                sb.append(" and "+key+" like ? ");
                params.add("%"+value+"%");//加条件的值
            }
        }
        //添加分页的查询
        sb.append(" limit ?,? ");
        //添加分页查询参数值
        params.add(start);
        params.add(rows);
        sql= sb.toString();
        System.out.println(params);
        System.out.println(sql);
        //查询
        return template.query(sql,new BeanPropertyRowMapper<User>(User.class),params.toArray());
    }
}
