import { useEffect, useState } from "react";
import LoginScreen from "./module/LoginScreen";
import Dashboard from "./module/Dashboard";

export default function App() {
  const [token, setToken] = useState<string | null>(null);

  useEffect(() => {
    const savedToken = localStorage.getItem("jwtToken");
    if (savedToken) {
      setToken(savedToken);
    }
  }, []);

  if (!token) {
    return <LoginScreen onLogin={setToken} />;
  }

  return <Dashboard token={token} setToken={setToken} />;
}