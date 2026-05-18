import React, { useMemo } from "react";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { MapPin, Cpu, Activity } from "lucide-react";
import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";
import { useLocations } from "@/hooks/useLocations";
import { useDevices } from "@/hooks/useDevices";

interface MainOverviewScreenProps {
  token: string;
  handleLogout: () => void;
}

const COLORS = [
  "#3b82f6",
  "#10b981",
  "#f59e0b",
  "#ef4444",
  "#8b5cf6",
  "#06b6d4",
];

// ---------- Main Component ----------
export const MainOverviewScreen: React.FC<MainOverviewScreenProps> = ({
  token,
  handleLogout,
}) => {
  // Fetch both resources in parallel, with token in the query key
  const { data: locations, isLoading: locLoading, isError: locError, error: locErr } = useLocations(token, handleLogout);
  const { data: devices, isLoading: devLoading, isError: devError, error: devErr } = useDevices(token, handleLogout);

  const isLoading = locLoading || devLoading;
  const isError = locError || devError;
  // Combine error messages into one (show first non-null)
  const errorMessage = locErr?.message || devErr?.message || "Unable to load dashboard data";

  // Default to empty arrays so memoised calculations never break
  const safeLocations = locations ?? [];
  const safeDevices = devices ?? [];

  /**
   * Devices grouped by location
   */
  const devicesByLocation = useMemo(() => {
    const locationMap = new Map<number, string>();
    safeLocations.forEach((location) => {
      locationMap.set(location.id, location.name);
    });

    const grouped: Record<string, number> = {};
    safeDevices.forEach((device) => {
      const locationName = locationMap.get(device.idLocation) || "Unknown";
      grouped[locationName] = (grouped[locationName] || 0) + 1;
    });

    return Object.entries(grouped).map(([name, value]) => ({
      name,
      value,
    }));
  }, [safeLocations, safeDevices]);

  /**
   * Devices grouped by model
   */
  const devicesByModel = useMemo(() => {
    const grouped: Record<string, number> = {};
    safeDevices.forEach((device) => {
      const modelName = `Model ${device.idModel}`;
      grouped[modelName] = (grouped[modelName] || 0) + 1;
    });

    return Object.entries(grouped).map(([name, value]) => ({
      name,
      value,
    }));
  }, [safeDevices]);

  const totalLocations = safeLocations.length;
  const totalDevices = safeDevices.length;
  const activeDevices = safeDevices.length; // temporary logic

  // ---------- Loading state ----------
  if (isLoading) {
    return (
      <div className="p-6 space-y-6">
        <Skeleton className="h-32 w-full" />
        <Skeleton className="h-96 w-full" />
      </div>
    );
  }

  // ---------- Error state ----------
  if (isError) {
    return (
      <div className="p-6">
        <Card>
          <CardContent className="py-8 text-center text-red-500">
            {errorMessage}
          </CardContent>
        </Card>
      </div>
    );
  }

  // ---------- Pie chart helper ----------
  const renderPieChart = (
    title: string,
    data: { name: string; value: number }[]
  ) => (
    <Card className="h-full">
      <CardHeader>
        <CardTitle>{title}</CardTitle>
      </CardHeader>
      <CardContent className="h-[320px]">
        {data.length === 0 ? (
          <div className="flex h-full items-center justify-center text-muted-foreground">
            No data available
          </div>
        ) : (
          <ResponsiveContainer width="100%" height="100%">
            <PieChart>
              <Pie
                data={data}
                dataKey="value"
                nameKey="name"
                outerRadius={100}
                label
              >
                {data.map((_, index) => (
                  <Cell
                    key={index}
                    fill={COLORS[index % COLORS.length]}
                  />
                ))}
              </Pie>
              <Tooltip />
              <Legend />
            </PieChart>
          </ResponsiveContainer>
        )}
      </CardContent>
    </Card>
  );

  return (
    <div className="w-full p-6 space-y-6">
      {/* Stats */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <StatCard
          title="Total Locations"
          value={totalLocations}
          icon={<MapPin className="h-5 w-5 text-blue-500" />}
        />
        <StatCard
          title="Total Devices"
          value={totalDevices}
          icon={<Cpu className="h-5 w-5 text-green-500" />}
        />
        <StatCard
          title="Active Devices"
          value={activeDevices}
          icon={<Activity className="h-5 w-5 text-orange-500" />}
        />
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 xl:grid-cols-2 gap-6">
        {renderPieChart("Devices by Location", devicesByLocation)}
        {renderPieChart("Devices by Model", devicesByModel)}
      </div>
    </div>
  );
};

// ---------- Stat card sub‑component ----------
interface StatCardProps {
  title: string;
  value: number;
  icon: React.ReactNode;
}

const StatCard: React.FC<StatCardProps> = ({ title, value, icon }) => {
  return (
    <Card>
      <CardContent className="flex items-center justify-between p-6">
        <div>
          <p className="text-sm text-muted-foreground">{title}</p>
          <h2 className="text-3xl font-bold mt-2">{value}</h2>
        </div>
        <div className="p-3 rounded-full bg-muted">{icon}</div>
      </CardContent>
    </Card>
  );
};