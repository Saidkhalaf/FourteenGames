import axios from "axios";
import {QueryClient} from "@tanstack/react-query";

export const queryClient = new QueryClient();
axios.defaults.baseURL = "/api"