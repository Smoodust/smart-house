import { Home, MonitorSmartphone } from "lucide-react";
import { MainOverviewScreen } from "./MainOverviewScreen";
import DevicePage from "./DevicePage";

const TABS = [
  { id: "main", label: "Main", icon: Home, screen: (token: string, handleLogout: () => void) => <MainOverviewScreen token={token} handleLogout={handleLogout} /> },
  { id: "device", label: "Devices", icon: MonitorSmartphone, screen: (token: string, handleLogout: () => void) => <DevicePage token={token} handleLogout={handleLogout} /> },
];

export default TABS;