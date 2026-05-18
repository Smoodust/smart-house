import { useState } from "react";
import Sidebar from "./Sidebar";
import TABS from "./TabsConstants";

export default function Dashboard({ token, setToken }: { token: string; setToken: (token: string | null) => void }) {
  const [activeTab, setActiveTab] = useState<string>("main");

  const handleLogout = () => {
    localStorage.removeItem("jwtToken");
    setToken(null);
  };

  return (
    <div className="flex min-h-screen bg-gray-900">
      <Sidebar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        onLogout={handleLogout}
      />
      {TABS.find((tab) => tab.id === activeTab)?.screen(token, handleLogout) || <div className="p-6">Tab not found</div>}
    </div>
  );
};