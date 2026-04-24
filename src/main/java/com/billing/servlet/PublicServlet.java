package com.billing.servlet;

import com.billing.db.DB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

// Public endpoints — no auth required. For browsing packages before signing up.
@WebServlet("/api/public/*")
public class PublicServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = req.getPathInfo();

        if (path == null || "/".equals(path) || path.startsWith("/rateplans")) {
            // GET /api/public/rateplans
            String sub = path != null ? path.replace("/rateplans", "") : "";
            if (sub.isEmpty() || "/".equals(sub)) {
                try {
                    sendJson(res, DB.executeSelect(
                        "SELECT id, name, price, ror_voice AS rorVoice, ror_data AS rorData, ror_sms AS rorSms " +
                        "FROM rateplan WHERE name IN ('Prepaid Standard', 'Premium Gold', 'Elite Enterprise') " +
                        "ORDER BY price ASC"
                    ));
                } catch (Exception e) {
                    sendError(res, 500, e.getMessage());
                }
            } else {
                // GET /api/public/rateplans/3
                try {
                    int id = Integer.parseInt(sub.substring(1));
                    List<Map<String, Object>> plan = DB.executeSelect("SELECT * FROM rateplan WHERE id = ?", id);
                    if (plan.isEmpty()) sendError(res, 404, "Rate plan not found");
                    else sendJson(res, plan.get(0));
                } catch (NumberFormatException e) {
                    sendError(res, 400, "Invalid ID");
                } catch (Exception e) {
                    sendError(res, 500, e.getMessage());
                }
            }
        } else if (path.startsWith("/service-packages")) {
            // GET /api/public/service-packages
            try {
                sendJson(res, DB.executeSelect(
                    "SELECT id, name, description, price, is_roaming AS isRoaming, type, amount, " +
                    "voice_amount AS voiceAmount, data_amount AS dataAmount, sms_amount AS smsAmount " +
                    "FROM service_package ORDER BY price ASC"
                ));
            } catch (Exception e) {
                sendError(res, 500, e.getMessage());
            }
        } else {
            sendError(res, 404, "Not found");
        }
    }
}
