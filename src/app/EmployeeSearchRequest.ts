// employee-search-request.model.ts
export interface EmployeeSearchRequest {
    name?: string;
    email?: string;
    gender?: string;
    location?: string;
    fromDate?: Date;
    toDate?: Date;
}
