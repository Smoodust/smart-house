import { useQuery } from "@tanstack/react-query";
import type { SettingsDefinitionDTO } from "@/module/Types";

const fetchDeviceModel = async (token: string, modelId: number, on403?: () => void): Promise<SettingsDefinitionDTO[]> => {
  const res = await fetch(`http://127.0.0.1:8080/model/${modelId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });   
  if (res.status === 403) {
    on403?.();
    throw new Error("Session expired");
  }
  if (!res.ok) throw new Error("Failed to fetch device model");
  return res.json();
};

export const useDeviceModels = (token: string, modelId: number, handleLogout?: () => void) => {
  return useQuery({
    queryKey: ["deviceModels", token, modelId],
    queryFn: () => fetchDeviceModel(token, modelId, handleLogout),
    staleTime: 5 * 60 * 1000,
  });
};