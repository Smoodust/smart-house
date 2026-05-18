import { useQuery } from "@tanstack/react-query";
import type { DeviceDTO } from "@/module/Types";

const fetchDevices = async (token: string, on403?: () => void): Promise<DeviceDTO[]> => {
  const res = await fetch("http://127.0.0.1:8080/devices", {
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });
  if (res.status === 403) {
    on403?.();
    throw new Error("Session expired");
  }
  if (!res.ok) throw new Error("Failed to fetch devices");
  const devices = await res.json();
  return devices.sort((a: DeviceDTO, b: DeviceDTO) =>
    a.name.localeCompare(b.name)
  );
};

export const useDevices = (token: string, handleLogout?: () => void) => {
  return useQuery({
    queryKey: ["devices", token],   
    queryFn: () => fetchDevices(token, handleLogout),
    staleTime: 5 * 60 * 1000,
  });
};