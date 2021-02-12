package controllers.reports;

import java.io.IOException;
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
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class FollowOnlyReportsIndexServlet
 */
@WebServlet("/FollowOnlyReportsIndexServlet")
public class FollowOnlyReportsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowOnlyReportsIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        EntityManager em = DBUtil.createEntityManager();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        List<Report> reports = em.createNamedQuery("getFollowedOnlyReports", Report.class)
                .setParameter("login_employee",login_employee)
                                  .setFirstResult(15 * (page - 1))
                                  .setMaxResults(15)
                                  .getResultList();

        long reports_count = (long)em.createNamedQuery("getReportsCount", Long.class)
                                     .getSingleResult();

      //現在ログインしているユーザーのIDをもとにフォローリストテーブルデータを取得
        List<Employee> follow_list =em.createNamedQuery("getMyfollowlist", Employee.class)
                .setParameter("id",login_employee)
                .getResultList();

        em.close();
      //現在ログインしてるユーザーが対象のemployeesリストの従業員をフォローしているかしていないかをMapで管理
        Map<Integer,Boolean> map = new HashMap<>();
        if(follow_list != null){
            for(Report report : reports)
            {
                map.put(report.getId(), false);
                for(Employee Followed : follow_list){
                    if(report.getEmployee().getId() == Followed.getId()){
                        map.put(report.getId(), true);
                        break;
                    }
                }
            }
        }
        request.setAttribute("RedirectURL",request.getServletPath());
        request.setAttribute("map", map);
        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("page", page);
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/followedonlyindex.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
