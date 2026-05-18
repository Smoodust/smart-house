import { LogOut, RefreshCw } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useQueryClient } from "@tanstack/react-query";
import TABS from "./TabsConstants";

interface SidebarProps {
  activeTab: string;
  setActiveTab: (id: string) => void;
  onLogout: () => void;
}

export default function Sidebar({ activeTab, setActiveTab, onLogout }: SidebarProps) {
  const queryClient = useQueryClient();

  const handleRefresh = async () => {
    await queryClient.invalidateQueries({
      type: "active",
    });
  };

  return (
    <div className="w-64 bg-gray-800 border-r border-gray-700 h-screen flex flex-col justify-between p-4">
      <div>
        <h1 className="text-xl font-bold mb-6 text-white">My Dashboard</h1>

        <div className="space-y-2">
          {TABS.map((tab) => {
            const Icon = tab.icon;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`w-full flex items-center gap-3 px-3 py-2 rounded-xl transition ${
                  activeTab === tab.id
                    ? "bg-gray-700 text-white"
                    : "text-gray-300 hover:bg-gray-700 hover:text-white"
                }`}
              >
                <Icon size={18} />
                {tab.label}
              </button>
            );
          })}
        </div>
      </div>

      <div className="space-y-2">
        {/* Refresh Button */}
        <Button
          variant="secondary"
          className="w-full flex items-center gap-2"
          onClick={handleRefresh}
        >
          <RefreshCw size={16} />
          Refresh
        </Button>

        <Button
          variant="destructive"
          className="w-full flex items-center gap-2"
          onClick={onLogout}
        >
          <LogOut size={16} />
          Logout
        </Button>
      </div>
    </div>
  );
};