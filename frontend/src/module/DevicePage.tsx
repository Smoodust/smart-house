import React, { useState, useEffect } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import { Textarea } from "@/components/ui/textarea";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Skeleton } from "@/components/ui/skeleton";
import { Alert, AlertDescription } from "@/components/ui/alert";
import {
  Loader2,
  Save,
  Code2,
  FormInput,
  AlertCircle,
  Check,
  ChevronRight,
} from "lucide-react";

import { useDevices } from "../hooks/useDevices";
import { useLocations } from "../hooks/useLocations";
import { useDeviceModels } from "../hooks/useDeviceModels";
import type { DeviceDTO, SettingsDefinitionDTO } from "./Types";

// --- Update mutation ---
async function updateDeviceSettings(
  token: string,
  deviceId: number,
  updatedMap: Record<string, any>,
  handleLogout?: () => void
): Promise<DeviceDTO | null> { // return type can be null if empty
  const res = await fetch(`http://127.0.0.1:8080/devices/${deviceId}`, {
    method: "PATCH",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(updatedMap),
  });

  if (res.status === 403) {
    handleLogout?.();
    throw new Error("Session expired");
  }

  if (!res.ok) {
    throw new Error("Failed to update device settings");
  }

  // If the response body is empty (e.g. 204 No Content, or 200 with empty body)
  const contentLength = res.headers.get("content-length");
  if (res.status === 204 || contentLength === "0") {
    return null;
  }

  // Optional: also check content-type for JSON
  const contentType = res.headers.get("content-type");
  if (contentType && contentType.includes("application/json")) {
    return res.json();
  }

  // Fallback: try to parse as JSON (may still throw if not JSON, but now less likely)
  // If your API never sends a body, you can just return null here.
  return null;
}

// --- Props for the main dashboard ---
interface DevicePageProps {
  token: string;
  handleLogout?: () => void;
}

// ----------------------------------------------------------------------
// Settings editor panel (only rendered when a device is selected)
// ----------------------------------------------------------------------
function DeviceSettingsPanel({
  device,
  token,
  handleLogout,
}: {
  device: DeviceDTO;
  token: string;
  handleLogout?: () => void;
}) {
  const queryClient = useQueryClient();
  const [editMode, setEditMode] = useState<"form" | "json">("form");
  const [formValues, setFormValues] = useState<Record<string, any>>({});
  const [jsonText, setJsonText] = useState("");
  const [jsonError, setJsonError] = useState<string | null>(null);

  // Fetch device model – now modelId is always > 0 because we have a valid device
  const {
    data: modelDefinitions,
    isLoading: modelLoading,
    error: modelError,
  } = useDeviceModels(token, device.idModel, handleLogout);

  // Initialize form state when device or model definitions change
  useEffect(() => {
    const currentMap = device.setting ?? {};
    console.log("Initializing form with device settings:", currentMap);
    setFormValues({ ...currentMap });
    setJsonText(JSON.stringify(currentMap, null, 2));
    setJsonError(null);
  }, [device]);

  // Update mutation
  const updateMutation = useMutation({
    mutationFn: (updatedMap: Record<string, any>) =>
      updateDeviceSettings(token, device.id, updatedMap, handleLogout),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["devices", token] });
    },
  });

  const handleFormInputChange = (key: string, value: any) => {
    setFormValues((prev) => ({ ...prev, [key]: value }));
  };

  const handleJsonChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setJsonText(e.target.value);
    setJsonError(null);
  };

  const handleUpdate = () => {
    if (editMode === "form") {
      updateMutation.mutate(formValues);
    } else {
      try {
        const parsed = JSON.parse(jsonText);
        if (typeof parsed !== "object" || parsed === null || Array.isArray(parsed)) {
          setJsonError("Settings must be a JSON object.");
          return;
        }
        updateMutation.mutate(parsed);
      } catch {
        setJsonError("Invalid JSON format.");
      }
    }
  };

  const renderFormInput = (def: SettingsDefinitionDTO) => {
    const value = formValues[def.name] ?? def.defaultValue;
    switch (def.type) {
      case "string":
        return (
          <Input
            id={`field-${def.name}`}
            type="text"
            value={value ?? ""}
            onChange={(e) => handleFormInputChange(def.name, e.target.value)}
          />
        );
      case "float":
        return (
          <Input
            id={`field-${def.name}`}
            type="number"
            step="any"
            value={value ?? ""}
            onChange={(e) =>
              handleFormInputChange(
                def.name,
                e.target.value === "" ? "" : parseFloat(e.target.value)
              )
            }
          />
        );
      case "boolean":
        return (
          <div className="flex items-center space-x-2">
            <Switch
              id={`field-${def.name}`}
              checked={!!value}
              onCheckedChange={(checked) =>
                handleFormInputChange(def.name, checked)
              }
            />
            <Label htmlFor={`field-${def.name}`} className="text-sm">
              {value ? "True" : "False"}
            </Label>
          </div>
        );
      default:
        return null;
    }
  };

  if (modelLoading) {
    return (
      <div className="p-4 space-y-3">
        <Skeleton className="h-4 w-1/3" />
        <Skeleton className="h-10 w-full" />
        <Skeleton className="h-4 w-1/3" />
        <Skeleton className="h-10 w-full" />
      </div>
    );
  }

  if (modelError) {
    return (
      <Alert variant="destructive" className="m-4">
        <AlertCircle className="h-4 w-4" />
        <AlertDescription>
          Could not load settings schema for this device model.
        </AlertDescription>
      </Alert>
    );
  }

  return (
    <div className="flex flex-col h-full">
      {/* Mode toggle */}
      <div className="flex items-center justify-between p-2 border-b">
        <h2 className="font-semibold text-lg truncate">{device.name}</h2>
        <div className="flex items-center space-x-1">
          <Button
            variant={editMode === "form" ? "default" : "outline"}
            size="sm"
            onClick={() => setEditMode("form")}
          >
            <FormInput className="h-4 w-4 mr-1" />
            Form
          </Button>
          <Button
            variant={editMode === "json" ? "default" : "outline"}
            size="sm"
            onClick={() => setEditMode("json")}
          >
            <Code2 className="h-4 w-4 mr-1" />
            JSON
          </Button>
        </div>
      </div>

      {/* Editing area */}
      <div className="flex-1 overflow-auto p-4">
        {editMode === "form" ? (
          <div className="space-y-4">
            {(modelDefinitions?.length ?? 0) === 0 ? (
              <p className="text-sm text-muted-foreground">
                This device model has no settings defined.
              </p>
            ) : (
              modelDefinitions!.map((def) => (
                <div key={def.name} className="space-y-1">
                  <Label htmlFor={`field-${def.name}`} className="text-sm font-medium">
                    {def.name} <span className="text-xs text-muted-foreground">({def.type})</span>
                  </Label>
                  {renderFormInput(def)}
                </div>
              ))
            )}
          </div>
        ) : (
          <div className="space-y-2">
            <Textarea
              className="font-mono text-sm min-h-[300px]"
              value={jsonText}
              onChange={handleJsonChange}
              placeholder="{}"
            />
            {jsonError && (
              <p className="text-sm text-destructive flex items-center">
                <AlertCircle className="h-4 w-4 mr-1" />
                {jsonError}
              </p>
            )}
          </div>
        )}
      </div>

      {/* Update button */}
      <div className="p-3 border-t">
        <Button
          onClick={handleUpdate}
          disabled={updateMutation.isPending}
          className="w-full"
        >
          {updateMutation.isPending ? (
            <Loader2 className="h-4 w-4 mr-2 animate-spin" />
          ) : updateMutation.isSuccess ? (
            <Check className="h-4 w-4 mr-2" />
          ) : (
            <Save className="h-4 w-4 mr-2" />
          )}
          {updateMutation.isPending
            ? "Updating..."
            : updateMutation.isSuccess
            ? "Updated"
            : "Update"}
        </Button>
        {updateMutation.isError && (
          <p className="text-sm text-destructive mt-2">
            <AlertCircle className="h-4 w-4 inline mr-1" />
            {(updateMutation.error as Error)?.message || "Update failed"}
          </p>
        )}
      </div>
    </div>
  );
}

// ----------------------------------------------------------------------
// Main Dashboard Component
// ----------------------------------------------------------------------
export default function DevicePage({
  token,
  handleLogout,
}: DevicePageProps) {
  const {
    data: locations,
    isLoading: locLoading,
    error: locError,
  } = useLocations(token, handleLogout);

  const {
    data: devices,
    isLoading: devLoading,
    error: devError,
  } = useDevices(token, handleLogout);

  const [selectedLocationId, setSelectedLocationId] = useState<number | null>(null);
  const [selectedDeviceId, setSelectedDeviceId] = useState<number | null>(null);

  // Default to first location when data loads
  useEffect(() => {
    if (locations && locations.length > 0 && selectedLocationId === null) {
      setSelectedLocationId(locations[0].id);
    }
  }, [locations, selectedLocationId]);

  // Reset device when location changes
  useEffect(() => {
    setSelectedDeviceId(null);
  }, [selectedLocationId]);

  const locationDevices = (devices || []).filter(
    (d) => d.idLocation === selectedLocationId
  );

  const selectedDevice = devices?.find((d) => d.id === selectedDeviceId) ?? null;

  // Left panel – device list
  const renderLeftPanel = () => {
    if (devLoading) {
      return (
        <div className="space-y-2 p-2">
          <Skeleton className="h-4 w-3/4" />
          <Skeleton className="h-4 w-1/2" />
          <Skeleton className="h-4 w-5/6" />
        </div>
      );
    }

    if (devError) {
      return (
        <Alert variant="destructive" className="m-2">
          <AlertCircle className="h-4 w-4" />
          <AlertDescription>Failed to load devices.</AlertDescription>
        </Alert>
      );
    }

    if (!locationDevices.length) {
      return (
        <p className="text-sm text-muted-foreground p-4 text-center">
          No devices in this location.
        </p>
      );
    }

    return (
      <ScrollArea className="h-full">
        <div className="p-2 space-y-1">
          {locationDevices.map((device) => (
            <Button
              key={device.id}
              variant={selectedDeviceId === device.id ? "secondary" : "ghost"}
              className="w-full justify-start font-normal"
              onClick={() => setSelectedDeviceId(device.id)}
            >
              <ChevronRight
                className={`mr-2 h-4 w-4 transition-transform ${
                  selectedDeviceId === device.id ? "rotate-90" : ""
                }`}
              />
              {device.name}
            </Button>
          ))}
        </div>
      </ScrollArea>
    );
  };

  // Loading / error states for locations
  if (locLoading) {
    return (
      <div className="flex h-full items-center justify-center">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    );
  }

  if (locError) {
    return (
      <div className="flex h-full items-center justify-center">
        <Alert variant="destructive" className="max-w-md">
          <AlertCircle className="h-4 w-4" />
          <AlertDescription>Failed to load locations. Please try again.</AlertDescription>
        </Alert>
      </div>
    );
  }

  if (!locations || locations.length === 0) {
    return (
      <div className="flex h-full items-center justify-center text-muted-foreground">
        No locations available.
      </div>
    );
  }

  return (
    <div className="h-full w-full flex flex-col p-2">
      <Tabs
        value={selectedLocationId?.toString() ?? ""}
        onValueChange={(val) => setSelectedLocationId(Number(val))}
        className="flex-1 flex flex-col"
      >
        <TabsList className="w-full justify-start flex-wrap h-auto">
          {locations.map((loc) => (
            <TabsTrigger key={loc.id} value={loc.id.toString()}>
              {loc.name}
            </TabsTrigger>
          ))}
        </TabsList>

        {locations.map((loc) => (
          <TabsContent
            key={loc.id}
            value={loc.id.toString()}
            className="flex-1 mt-2 border rounded-md overflow-hidden"
          >
            <div className="flex h-full">
              {/* Left device list */}
              <div className="w-1/4 min-w-[200px] max-w-xs border-r bg-card">
                {renderLeftPanel()}
              </div>
              {/* Right side: only render editor if a device is selected */}
              <div className="flex-1">
                {selectedDevice ? (
                  <DeviceSettingsPanel
                    device={selectedDevice}
                    token={token}
                    handleLogout={handleLogout}
                  />
                ) : (
                  <div className="flex items-center justify-center h-full text-muted-foreground">
                    <FormInput className="mr-2 h-6 w-6" />
                    <span>Select a device from the left panel to edit its settings.</span>
                  </div>
                )}
              </div>
            </div>
          </TabsContent>
        ))}
      </Tabs>
    </div>
  );
}