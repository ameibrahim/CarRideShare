// app/api/rides/route.ts
import { NextResponse } from "next/server";
import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

export async function GET() {
    const rides = await prisma.ride.findMany();
    return NextResponse.json(rides);
}

export async function POST(request: Request) {
    const body = await request.json();
    const {
        createdById,
        startLocation,
        name,
        endLocation,
        taxiCalled,
        taxiArrivalTime,
        cost,
        totalSeats,
        availableSeats,
        status,
    } = body;

    try {
        // Compute cost per passenger
        const costPerPassenger = totalSeats > 0 ? cost / totalSeats : cost;

        const ride = await prisma.ride.create({
            data: {
                createdById,
                startLocation,
                name: name || `Ride to ${endLocation}`,
                endLocation,
                taxiCalled,
                taxiArrivalTime: taxiArrivalTime || "",
                cost,
                totalSeats,
                availableSeats,
                status,
                // Create an initial booking for the creator of the ride
                bookings: {
                    create: {
                        userId: createdById,
                        costPerPassenger: costPerPassenger,
                    },
                },
            },
            include: {
                bookings: true, // Return the bookings in the response
            },
        });
        return NextResponse.json(ride, { status: 201 });
    } catch (error) {
        console.log("error: ", error);
        return NextResponse.json(
            { error: "Failed to create ride" },
            { status: 500 }
        );
    }
}
