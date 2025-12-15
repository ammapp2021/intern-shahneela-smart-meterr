## **Project Overview**
**The Smart Meter Reading and Billing System** collects and processes electricity consumption data from smart meters in real time.  
It **aggregates readings daily, weekly, and monthly**, automatically calculating consumption and bills.  
Users can **monitor live usage** through an interactive dashboard with alerts and detailed meter information.

## **Smoke Test**
1. **Simulate meter readings** by sending sample data to the smart-meter service.  
2. **Verify aggregates**: Check that daily, weekly, and monthly totals are calculated correctly in the database.  
3. **Open live dashboard** and confirm real-time meter updates via SSE.  
4. **Check alerts**: Ensure notifications are triggered when consumption exceeds predefined thresholds.  
5. **Inspect individual meter details** to confirm correct total units and bills are displayed.

## **Instructions to Run the Project**
1. `docker-compose build`  
2. `docker-compose up -d`  
3. `docker compose up`  
4. Run **UI locally** via VS Code. => npm install -> npm run dev 
5. Open the UI at: [http://localhost:5173](http://localhost:5173)
