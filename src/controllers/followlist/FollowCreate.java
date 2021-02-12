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
 * Servlet implementation class FollowCreate
 */
@WebServlet("/FollowCreate")
public class FollowCreate extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowCreate() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       EntityManager em = DBUtil.createEntityManager();

       String SearchName =request.getParameter("employee_name");
       Follow follow = new Follow();
       Employee FollowId = (Employee) em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
       follow.setFollowedID((Employee)request.getSession().getAttribute("login_employee"));
       follow.setFollowerID(FollowId);

       em.getTransaction().begin();
       em.persist(follow);
       em.getTransaction().commit();
       request.getSession().setAttribute("SearchName", SearchName);
       request.getSession().setAttribute("flush", FollowId.getName() +"をフォローしました。");
       em.close();
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
