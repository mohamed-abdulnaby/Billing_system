package com.billing.servlet;

import com.billing.db.DB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/admin/customers/*")
public class AdminUserServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handle(res, () -> {
            String pathParam = getPathParam(req);
            if (pathParam == null) {
                return DB.executeSelect("SELECT * FROM get_all_customers()");
            } else {
                int id = Integer.parseInt(pathParam);
                List<Map<String, Object>> list = DB.executeSelect("SELECT * FROM get_customer_by_id(?)", id);
                if (list.isEmpty()) throw new RuntimeException("Customer not found");
                return list.get(0);
            }
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handle(res, () -> {
            Map<String, Object> data = readJson(req);

            String username  = (String) data.get("username");
            String password  = (String) data.get("password");
            String role      = (String) data.get("role");
            String name      = (String) data.get("name");
            String email     = (String) data.get("email");
            String address   = (String) data.get("address");
            String birthdate = (String) data.get("birthdate");

            if (username == null || username.isBlank())
                throw new RuntimeException("username is required");
            if (password == null || password.isBlank())
                throw new RuntimeException("password is required");
            if (email == null || email.isBlank())
                throw new RuntimeException("email is required");

            // Use provided password instead of hardcoded value
            List<Map<String, Object>> result = DB.executeSelect(
                    "SELECT create_customer(?, ?, ?, ?, ?, ?::DATE) AS id",
                    username,
                    password,
                    name,
                    email,
                    address,
                    birthdate
            );

            int newId = ((Number) result.get(0).get("id")).intValue();

            // Update role if provided and different from default
            if (role != null && !role.isBlank() && !role.equals("customer")) {
                DB.executeSelect("UPDATE user_account SET role = ? WHERE id = ?", role, newId);
            }

            return Map.of("success", true, "id", newId, "message", "Customer created successfully");
        });
    }
}
