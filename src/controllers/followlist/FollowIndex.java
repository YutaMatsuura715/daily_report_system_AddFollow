package controllers.followlist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class FollowListIndex
 */
@WebServlet("/FollowList/Index")
public class FollowIndex extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowIndex() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        List <Employee> employees = new ArrayList<Employee>();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        int page = 1;
        String SearchName = request.getParameter("employee_name");

        if(request.getSession().getAttribute("SearchName") != null){
            SearchName = (String)request.getSession().getAttribute("SearchName");
        }

        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) { }
       if(SearchName  == null || SearchName.length() == 0){
           SearchName = "";
       }
        //名前をもとにデータベースから部分一致で従業員リストを取得
        employees = em.createNamedQuery("SearchName", Employee.class)
                .setParameter("Name", "%" + SearchName + "%")
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();

        //名前検索で取得したデータベースのカウント
        long employees_count = (long)em.createNamedQuery("getEmployeesCountNameSearch", Long.class)
                .setParameter("Name", "%" + SearchName + "%")
                .getSingleResult();


        //現在ログインしているユーザーのIDをもとにフォローリストテーブルデータを取得
        List<Employee> follow_list =em.createNamedQuery("getMyfollowlist", Employee.class)
                .setParameter("id",login_employee)
                .getResultList();
        em.close();

        //現在ログインしてるユーザーが対象のemployeesリストの従業員をフォローしているかしていないかをMapで管理
        Map<Integer,Boolean> map = new HashMap<>();
        if(follow_list != null){
            for(Employee employee : employees)
            {
                map.put(employee.getId(), false);
                for(Employee Followed : follow_list){
                    if(employee.getId() == Followed.getId()){
                        map.put(employee.getId(), true);
                        break;
                    }

                }

            }
        }
        request.setAttribute("RedirectURL",request.getServletPath());
        request.setAttribute("employees", employees);
        request.setAttribute("employees_count", employees_count);
        request.setAttribute("page", page);
        request.setAttribute("map", map);
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }
        if(request.getSession().getAttribute("SearchName") != null) {
            request.setAttribute("SearchName", request.getSession().getAttribute("SearchName"));
            request.getSession().removeAttribute("SearchName");
        }else{
            request.setAttribute("SearchName",SearchName);
        }


        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/followlist/index.jsp");
        rd.forward(request, response);
    }




}
