package cn.itcast.service.impl;

import cn.itcast.dao.UserDao;
import cn.itcast.dao.impl.UserDaoImpl;
import cn.itcast.domain.PageBean;
import cn.itcast.domain.User;
import cn.itcast.service.UserService;

import java.util.List;
import java.util.Map;

public class UserServiceImpl  implements UserService {
    //接口那类型，new一个实现类
    private UserDao dao = new UserDaoImpl();

    @Override
    public List<User> findAll() {
        //调用DAO完成查询,,直接查询所有数据库，返回给servlet

        return dao.findAll();
    }

    @Override
    public User login(User user) {
        return dao.findUserByUsernameAndPassword(user.getUsername(),user.getPassword());
    }

    @Override
    public void addUser(User user) {

        dao.add(user);
    }

    /**
     * 创建方法出来
     * 调用dao传过去，并且转换为Integer
     * @param id
     */
    @Override
    public void deleteUser(String id) {
        //
        dao.delete(Integer.parseInt(id));
    }

    @Override
    public User findUserById(String id) {
        return dao.findById(Integer.parseInt(id));
    }

    @Override
    public void updateUser(User user) {
        dao.update(user);
    }

    /**
     * 方法的实现
     * @param ids
     */

    @Override
    public void delSelectedUser(String[] ids) {
        if (ids !=null && ids.length>0){
            //1.遍历数组
            for (String id : ids){
                //调用dao
                dao.delete(Integer.parseInt(id));
            }
        }



    }

    @Override
    public PageBean<User> findUserByPage(String _currentPage, String _rows, Map<String, String[]> condition) {
        int currentPage = Integer.parseInt(_currentPage);
        int rows= Integer.parseInt(_rows);
        //导航条不能选择第一页之前的按钮
        if (currentPage <= 0){
            currentPage = 1;
        }

        //1创建一个空的PageBean对此
        PageBean<User> pb = new PageBean<User>();
        //2设置参数

        pb.setCurrentPage(currentPage);
        pb.setCurrentPage(rows);

        //3调用dao查询总记录数
       int totalCount =  dao.findTotalCount(condition);
       pb.setTotalCount(totalCount);
       //4调用dao查询list
        //计算开始记录的索引
        int start = (currentPage - 1) * rows;
        List<User> list=dao.findByPage(start,rows,condition);
        pb.setList(list);
        //5计算总页码
        int totalPage =totalCount % rows  == 0 ? totalCount/rows : (totalCount/rows)+1;
        pb.setTotalPage(totalPage);

        return pb;
    }
}
