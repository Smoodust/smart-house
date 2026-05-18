import { useQuery } from "@tanstack/react-query";
import type { LocationDTO } from "@/module/Types";

const fetchLocations = async (token: string, on403?: () => void): Promise<LocationDTO[]> => {
  const res = await fetch("http://127.0.0.1:8080/locations", {
    headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
  });
  if (res.status === 403) {
    on403?.();
    throw new Error("Session expired");
  }
  if (!res.ok) throw new Error("Failed to fetch locations");
  return res.json();
};

export const useLocations = (token: string, handleLogout?: () => void) => {
  return useQuery({
    queryKey: ["locations", token], 
    queryFn: () => fetchLocations(token, handleLogout),
    staleTime: 5 * 60 * 1000,
  });
};