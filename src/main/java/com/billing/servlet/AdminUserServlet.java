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
                return DB.executeSelect("SELECT id, username as msisdn, name, email, role, address, birthdate, category FROM user_account WHERE role = 'customer' ORDER BY id DESC");
            } else {
                int id = Integer.parseInt(pathParam);
                List<Map<String, Object>> list = DB.executeSelect("SELECT id, username as msisdn, name, email, role, address, birthdate, category FROM user_account WHERE id = ?", id);
                if (list.isEmpty()) throw new RuntimeException("Customer not found");
                return list.get(0);
            }
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handle(res, () -> {
            Map<String, Object> data = readJson(req);
            String msisdn = (String) data.get("msisdn");
            String category = (String) data.getOrDefault("category", "Silver");
            
            DB.executeUpdate(
                "INSERT INTO user_account (username, password, name, email, role, address, birthdate, category) VALUES (?, ?, ?, ?, 'customer', ?, ?, ?)",
                msisdn,
                "customer123", 
                data.get("name"),
                data.get("email"),
                data.get("address"),
                data.get("birthdate"),
                category
            );
            return Map.of("success", true, "message", "Customer created successfully");
        });
    }
}
