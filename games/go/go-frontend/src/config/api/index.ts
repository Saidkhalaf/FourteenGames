import {QueryClient} from "@tanstack/react-query";
import axios from "axios";

export const queryClient = new QueryClient();

//This url is used to gather content from our backend. The proxy itself is configured in vite.config.ts.
axios.defaults.baseURL = "/go/api"