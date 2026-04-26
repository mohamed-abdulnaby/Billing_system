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
        String path = req.getPathInfo();
        try {
            if (path == null || "/".equals(path)) {
                String sql = "SELECT * FROM get_all_contracts()";
                sendJson(res, DB.executeSelect(sql));
            } else {
                int id = Integer.parseInt(path.substring(1));
                String sql = "SELECT * FROM get_contract_by_id(?)";
                List<Map<String, Object>> list = DB.executeSelect(sql, id);
                if (list.isEmpty()) sendError(res, 404, "Contract not found");
                else sendJson(res, list.get(0));
            }
        } catch (Exception e) {
            sendError(res, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            Map body = readJson(req, Map.class);

            int userId      = ((Number) body.get("userId")).intValue();
            int ratePlanId  = ((Number) body.get("ratePlanId")).intValue();
            String msisdn   = (String) body.get("msisdn");
            double creditLimit = ((Number) body.get("creditLimit")).doubleValue();

            List<Map<String, Object>> result = DB.executeSelect(
                    "SELECT create_contract(?::integer, ?::integer, ?, ?::numeric) AS id",
                    userId,
                    ratePlanId,
                    msisdn,
                    creditLimit
            );
            res.setStatus(201);
            sendJson(res, result.get(0));
        } catch (Exception e) {
            sendError(res, 500, e.getMessage());
        }
    }
}
