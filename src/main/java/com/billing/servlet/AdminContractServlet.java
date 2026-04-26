package com.billing.servlet;

import com.billing.db.DB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/admin/contracts/*")
public class AdminContractServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handle(res, () -> {
            String path = req.getPathInfo();
            if (path == null || "/".equals(path)) {
                String sql = "SELECT c.id, c.msisdn, c.status, c.available_credit as \"availableCredit\", " +
                             "u.name as \"customerName\", r.name as \"rateplanName\" " +
                             "FROM contract c " +
                             "JOIN user_account u ON c.user_account_id = u.id " +
                             "LEFT JOIN rateplan r ON c.rateplan_id = r.id " +
                             "ORDER BY c.id DESC";
                return DB.executeSelect(sql);
            } else {
                int id = Integer.parseInt(path.substring(1));
                String sql = "SELECT c.*, u.name as \"customerName\", r.name as \"rateplanName\", " +
                             "c.available_credit as \"availableCredit\" " +
                             "FROM contract c " +
                             "JOIN user_account u ON c.user_account_id = u.id " +
                             "LEFT JOIN rateplan r ON c.rateplan_id = r.id " +
                             "WHERE c.id = ?";
                List<Map<String, Object>> list = DB.executeSelect(sql, id);
                if (list.isEmpty()) throw new RuntimeException("Contract not found");
                return list.get(0);
            }
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handle(res, () -> {
            Map<String, Object> body = readJson(req);
            String msisdn = (String) body.get("msisdn");
            Object userId = body.get("userId");
            Object planId = body.get("planId");
            
            if (msisdn == null || userId == null || planId == null) {
                throw new RuntimeException("Missing required fields: msisdn, userId, or planId");
            }

            DB.executeUpdate(
                "INSERT INTO contract (user_account_id, rateplan_id, msisdn, status, credit_limit, available_credit) " +
                "VALUES (?, ?, ?, 'active', 1000.0, 1000.0)",
                userId, planId, msisdn
            );
            return Map.of("success", true, "message", "Line provisioned for " + msisdn);
        });
    }
}
