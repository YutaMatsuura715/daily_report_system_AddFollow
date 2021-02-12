package controllers.followlist;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class FollowDestroy
 */
@WebServlet("/FollowDestroy")
public class FollowDestroy extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowDestroy() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        String SearchName =request.getParameter("employee_name");
        Employee FollowerID =em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
        Employee FollowedID = (Employee)request.getSession().getAttribute("login_employee");
        Follow rmFollow = (Follow) em.createNamedQuery("getfolowerID")
                                             .setParameter("FollowerID", FollowerID)
                                             .setParameter("FollowedID", FollowedID)
                                             .getSingleResult();


        em.getTransaction().begin();
        em.remove(rmFollow);
        em.getTransaction().commit();
        em.close();
        System.out.println(request.getContextPath() + request.getParameter("RedirectURL"));
        request.getSession().setAttribute("flush", FollowerID.getName() + "のフォローを外しました。");
        request.getSession().setAttribute("SearchName", SearchName);
        response.sendRedirect(request.getContextPath() + request.getParameter("RedirectURL"));

     }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
